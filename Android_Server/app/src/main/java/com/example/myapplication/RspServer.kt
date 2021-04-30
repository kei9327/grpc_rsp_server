package com.example.myapplication

import android.content.Context
import google.example.*
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.util.*

class RspServer(private val port: Int = 8080, val context: Context) {
    private var server: Server? = null
    private val service: RspGameService = RspGameServiceImpl()

    @Throws(IOException::class)
    fun start() {
        server = ServerBuilder.forPort(port)
            .addService(RspApplicationImpl(service))
//                    .addService(ProtoReflectionService.newInstance())
            .build()
            .start()

        println("Server started on port: $port")
        println("Server started on IP Address: ${getIPAddress(true)}")
        println("Server started on Wifi IP Address: ${wifiIpAddress(context)}")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                println("Shutting down gRPC server...")
                this@RspServer.stop()
                println("Server shut down.")
            }
        })
    }

    private fun stop() = server?.shutdown()

    /** Await termination on the main thread since the grpc library uses daemon threads. */
    @Throws(InterruptedException::class)
    public fun blockUntilShutdown() = server?.awaitTermination()

    private inner class RspApplicationImpl(val service: RspGameService) :
        RspApplicationGrpc.RspApplicationImplBase() {
        init {
            service.setGameCallback(object : RspGameCallback {
                override fun gameResult(
                    player: List<GRequest.Player>,
                    point: Int,
                    hostPlayer: GRequest.Player?
                ) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.RESULT).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(hostPlayer.name)
                                        .setIp(hostPlayer.ip).build()
                                )
                            }

                            player.forEach {
                                this.setResult(GResponse.Result.newBuilder().addWinner(it.name))
                            }
                        }.build()
                    sendMessage(response)
                }

                override fun gameTimeout(hostPlayer: GRequest.Player?) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.TIMEOUT).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(hostPlayer.name)
                                        .setIp(hostPlayer.ip).build()
                                )
                            }
                        }
                            .setTimeoutInfo(GResponse.Timeout.YES).build()
                    sendMessage(response)
                }

                override fun startGame(
                    player: List<GRequest.Player>,
                    hostPlayer: GRequest.Player?
                ) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.START)
                            .setTimeoutCount(20).apply {
                                hostPlayer?.let {
                                    setHostPlayer(
                                        Gamer.newBuilder().setName(hostPlayer.name)
                                            .setIp(hostPlayer.ip).build()
                                    )
                                }
                                player.forEach {
                                    this.addPlayer(
                                        GResponse.Player.newBuilder().setIp(it.ip).setName(it.name)
                                            .build()
                                    )
                                }
                            }.build()
                    sendMessage(response)
                }

                override fun gameDraw(hostPlayer: GRequest.Player?) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.DRAW).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(hostPlayer.name)
                                        .setIp(hostPlayer.ip).build()
                                )
                            }
                        }.build()
                    sendMessage(response)
                }

                override fun leavePlayer(key: GRequest.Player?) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.LEAVE).build()
                    sendMessage(response)
                }

                override fun ready2Play(hostPlayer: GRequest.Player?) {
                    sendReady2Play()
                }

                override fun changeHost(hostPlayer: GRequest.Player) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.CHANGE_HOST)
                            .setHostPlayer(
                                Gamer.newBuilder().setName(hostPlayer.name)
                                    .setIp(hostPlayer.ip).build()
                            ).build()
                    sendMessage(response)
                }

            })
        }

        fun sendReady2Play() { // 대기 중인 유저한데 게임 준비 안내
            loginClients.forEach {
                val response = Welecom.newBuilder().setCtype(service.isHost(it.key))
                    .setStatus(Welecom.Status.READY).build()
                loginClients[it.key]?.onNext(response)
                loginClients[it.key]?.onCompleted()
            }

            loginClients.clear()

        }

        fun sendMessage(response: GResponse) {
            clients.values.forEach { it1 -> it1.onNext(response) }
        }

        private val clients =
            Collections.synchronizedMap(mutableMapOf<String, StreamObserver<GResponse>>())
        private val clientUser =
            Collections.synchronizedMap(mutableMapOf<String, GRequest.Player>())

        override fun game(responseObserver: StreamObserver<GResponse>): StreamObserver<GRequest> {
            val id = UUID.randomUUID().toString()
            clients[id] = responseObserver

            println("Client connected with id $id")

            return streamObserver {

                onNext {
                    clientUser[id] = it.player
                    proxyMessage(it)
                    println("Received message from $id, sending to ${clients.size} clients")
                }

                onError {
                    println("Client error : ${it?.message}")
                    service.leftPlayer(clientUser[id])
                    clients.remove(id)
                    clientUser.remove(id)
                }

                onCompleted {
                    println("Client shut down.")
                    println("Client $id disconnected")
                    service.leftPlayer(clientUser[id])
                    clients.remove(id)
                    clientUser.remove(id)
                }

            }
        }

        private val loginClients =
            Collections.synchronizedMap(mutableMapOf<Gamer, StreamObserver<Welecom>>())

        override fun login(request: Gamer, responseObserver: StreamObserver<Welecom>) {
            val id = request
            loginClients[id] = responseObserver

            println("Client login : ${request.name} (${request.ip})")

            service.joinPlayer(request)

            val host = service.isHost(request)
            val playing = service.isPlaying()
            val response: Welecom = Welecom.newBuilder().setCtype(host)
                .setStatus(playing).build()

            println("Client login response : $response")

            responseObserver.onNext(response)

            if (playing == Welecom.Status.READY) {
                loginClients.remove(id)
                responseObserver.onCompleted()
            }
        }

        override fun rank(request: Gamer, responseObserver: StreamObserver<RankList>) {
            RankList.newBuilder().apply {
                var count = 1
                service.gameRank().forEach {
                    addRanker(
                        Ranker.newBuilder()
                            .setName(it.first.name)
                            .setScore(it.second)
                            .setRanking(count++)
                            .build()
                    ).build()
                }
            }.build().run {
                responseObserver.onNext(this)
                responseObserver.onCompleted()
            }
        }

        private fun proxyMessage(response: GRequest) {
            println("Client Message : $response")

            when (response.messageType) {
                GRequest.MessageType.HOST_GAME_START -> {
                    service.startHost(response.player)
                }
                GRequest.MessageType.SELECT -> {
                    service.select(response.player, response.select)
                }
                GRequest.MessageType.TIMEOUT_ACK -> {
                    service.timeoutCheck(response.player)
                }
            }
        }
    }
}