package com.example.myapplication

import android.content.Context
import google.example.*
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RspServer(private val port: Int = 31249, val context: Context, val listener: RspServerListener) {
    private var server: Server? = null
    private val service: RspGameService = RspGameServiceImpl()

    interface RspServerListener {
        fun serverInfo(ip:String, status: String)
        fun serverLog(log:String)
    }

    @Throws(IOException::class)
    fun start() {
        server = ServerBuilder.forPort(port)
            .addService(RspApplicationImpl(service))
//                    .addService(ProtoReflectionService.newInstance())
            .build()
            .start()

        log("Server started on port: $port")
        log("Server started on IP Address: ${getIPAddress(true)}")
        log("Server started on Wifi IP Address: ${wifiIpAddress(context)}")

        listener.serverInfo("IP : ${getIPAddress(true)}:$port \n ${wifiIpAddress(context)}:$port",
            "Server Running"
        )

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                log("Shutting down gRPC server...")
                this@RspServer.stop()
                log("Server shut down.")
            }
        })
    }

    fun log(log: String) {
        println(log)
        listener.serverLog(log)
    }

    fun stop() = server?.shutdown()

    /** Await termination on the main thread since the grpc library uses daemon threads. */
    @Throws(InterruptedException::class)
    fun blockUntilShutdown() = server?.awaitTermination()

    private inner class RspApplicationImpl(val service: RspGameService) :
        RspApplicationGrpc.RspApplicationImplBase() {
        init {
            service.setGameCallback(object : RspGameCallback {
                override fun gameResult(
                    player: ArrayList<Pair<String, String>>,
                    point: Int,
                    hostPlayer: Pair<String, String>?
                ) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.RESULT).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(it.second)
                                        .setIp(it.first).build()
                                )
                            }

                            player.forEach {
                                this.setResult(GResponse.Result.newBuilder().addWinner(it.second))
                            }
                        }.build()
                    sendMessage(response)
                }

                override fun gameTimeout(hostPlayer: Pair<String, String>?) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.TIMEOUT).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(hostPlayer.second)
                                        .setIp(hostPlayer.first).build()
                                )
                            }
                        }
                            .setTimeoutInfo(GResponse.Timeout.YES).build()
                    sendMessage(response)
                }

                override fun startGame(
                    player: HashMap<String, String>,
                    hostPlayer: Pair<String, String>?
                ) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.START)
                            .setTimeoutCount(20).apply {
                                hostPlayer?.let {
                                    setHostPlayer(
                                        Gamer.newBuilder().setName(it.second)
                                            .setIp(it.first).build()
                                    )
                                }
                                player.forEach {
                                    this.addPlayer(
                                        GResponse.Player.newBuilder().setIp(it.key).setName(it.value)
                                            .build()
                                    )
                                }
                            }.build()
                    sendMessage(response)
                }

                override fun gameDraw(hostPlayer: Pair<String, String>?) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.DRAW).apply {
                            hostPlayer?.let {
                                setHostPlayer(
                                    Gamer.newBuilder().setName(it.second)
                                        .setIp(it.first).build()
                                )
                            }
                        }.build()
                    sendMessage(response)
                }

                override fun leavePlayer(key: Pair<String, String?>) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.LEAVE).build()
                    sendMessage(response)
                }

                override fun ready2Play(hostPlayer: Pair<String, String>?) {
                    sendReady2Play()
                }

                override fun changeHost(hostPlayer: Pair<String, String>) {
                    val response: GResponse =
                        GResponse.newBuilder().setMessageType(GResponse.MessageType.CHANGE_HOST)
                            .setHostPlayer(
                                Gamer.newBuilder().setName(hostPlayer.second)
                                    .setIp(hostPlayer.first).build()
                            ).build()
                    sendMessage(response)
                }

            })
        }

        fun sendReady2Play() { // ?????? ?????? ???????????? ?????? ?????? ??????
            loginClients.forEach {
                val response = Welecom.newBuilder().setCtype(service.isHost(it.key))
                    .setStatus(Welecom.Status.READY).build()
                loginClients[it.key]?.onNext(response)
                loginClients[it.key]?.onCompleted()
            }

            loginClients.clear()

        }

        fun sendMessage(response: GResponse) {
            log("send response : $response")
            clients.values.forEach { it1 -> it1.onNext(response) }
        }

        private val clients =
            Collections.synchronizedMap(mutableMapOf<String, StreamObserver<GResponse>>())
        private val clientUser =
            Collections.synchronizedMap(mutableMapOf<String, GRequest.Player>())

        override fun game(responseObserver: StreamObserver<GResponse>): StreamObserver<GRequest> {
            val id = UUID.randomUUID().toString()
            clients[id] = responseObserver

            log("Client connected with id $id")

            return streamObserver {

                onNext {
                    clientUser[id] = it.player
                    proxyMessage(it)
                    log("Received message from $id, sending to ${clients.size} clients")
                }

                onError {
                    log("Client error : ${it?.message}")
                    service.leftPlayer(clientUser[id])
                    clients.remove(id)
                    clientUser.remove(id)
                }

                onCompleted {
                    log("Client shut down.")
                    log("Client $id disconnected")
                    service.leftPlayer(clientUser[id])
                    clients.remove(id)
                    clientUser.remove(id)
                }

            }
        }

        private val loginClients =
            Collections.synchronizedMap(mutableMapOf<String, StreamObserver<Welecom>>())

        override fun login(request: Gamer, responseObserver: StreamObserver<Welecom>) {
            val id = request
            loginClients[id.ip] = responseObserver

            log("Client login : ${request.name} (${request.ip})")

            service.joinPlayer(request)

            val host = service.isHost(request.ip)
            val playing = service.isPlaying()
            val response: Welecom = Welecom.newBuilder().setCtype(host)
                .setStatus(playing).build()

            log("Client login response : $response")

            responseObserver.onNext(response)

            if (playing == Welecom.Status.READY) {
                loginClients.remove(id.ip)
                responseObserver.onCompleted()
            }
        }

        override fun rank(request: Gamer, responseObserver: StreamObserver<RankList>) {
            RankList.newBuilder().apply {
                var count = 1
                service.gameRank().forEach {
                    addRanker(
                        Ranker.newBuilder()
                            .setName(it.first)
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
            log("Client Message : $response")

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