package example

import google.example.GRequest
import google.example.Gamer
import google.example.Welecom
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timer

class RspGameServiceImpl : RspGameService {

    val scoreBoard: HashMap<GRequest.Player, Int> = HashMap()
    private val playersList: ArrayList<GRequest.Player> = ArrayList()
    private val timeoutCheckList: ArrayList<GRequest.Player> = ArrayList()

    var hostPlayer: GRequest.Player? = null

    val gameBoard: HashMap<GRequest.Player, GRequest.Select> = HashMap()

    var callback: RspGameCallback? = null

    var time = 0
    var timerTask: Timer? = null
    var isPlaying = false

    override fun joinPlayer(gamer: Gamer) {
        val newPlayer = GRequest.Player.newBuilder().setIp(gamer.ip).setName(gamer.name).build()

        if (!checkSameUser(newPlayer)) {
            playersList.add(newPlayer)
        }
        if (playersList.size == 1) hostPlayer = newPlayer

        checkUserLog()
    }

    fun checkUserLog() {
        println("=========== Players ===========")
        playersList.forEach {
            println("${it.name} , ${it.ip} ")
        }
        println("=========== Players End ===========")
    }

    fun checkSameUser(player: GRequest.Player): Boolean {
        var result = false
        playersList.forEach {
            if (it.ip == player.ip && it.name == player.name) {
                result = true
                return@forEach
            }
        }

        return result
    }

    override fun leftPlayer(player: GRequest.Player?) {
        player?.let {
            playersList.remove(player)

            if (player.ip == hostPlayer?.ip &&
                player.name == hostPlayer?.name
            ) {
                if (playersList.isNotEmpty()) {
                    hostPlayer = playersList[0]
                    callback?.changeHost(playersList[0])
                } else {
                    hostPlayer = null
                }
            }
            checkUserLog()
        }
    }

    override fun startHost(player: GRequest.Player): Boolean {
        checkUserLog()

        return if (player.ip == hostPlayer?.ip &&
            player.name == hostPlayer?.name
        ) {
            gameBoard.clear()
            playersList.forEach {
                gameBoard[it] = GRequest.Select.NONE
            }

            timeoutCheckList.clear()
            timeoutCheckList.addAll(playersList)

            time = 0
            callback?.startGame(playersList, hostPlayer)
            isPlaying = true
            startTimer()
            true
        } else {
            false
        }
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {
            time++


            if (time == 20) {
                callback?.gameTimeout(hostPlayer)
            }

            if (time == 20 + 3) {
                timerTask?.cancel()
                processGame()
            }
        }
    }

    private fun processGame() {
        var scissors = ArrayList<GRequest.Player>()
        var rocks = ArrayList<GRequest.Player>()
        var paper = ArrayList<GRequest.Player>()

        gameBoard.entries.forEach { it ->
            when (it.value) {
                GRequest.Select.NONE -> {
                    playersList.remove(it.key)
                    callback?.leavePlayer(it.key)
                }
                GRequest.Select.ROCK -> rocks.add(it.key)
                GRequest.Select.PAPER -> paper.add(it.key)
                GRequest.Select.SCISSOR -> scissors.add(it.key)
            }
        }

        if (scissors.size == 0 && rocks.size != 0 && paper.size != 0) { // 가위 없음 => 보랑 바위만 남음 무조건 보 승
            gameResult(paper)
        } else if (rocks.size == 0 && scissors.size != 0 && paper.size != 0) { // 바위 없음 => 가위랑 보만 남음 무조건 가위 승
            gameResult(scissors)
        } else if (paper.size == 0 && rocks.size != 0 && scissors.size != 0) { // 보 없음 => 가위랑 바위만 남음 무조건 바위 승
            gameResult(rocks)
        } else { // 이외 모든 케이스는 비김
            callback?.gameDraw(hostPlayer)
        }
        isPlaying = false
        processRetire()
    }

    private fun gameResult(players: ArrayList<GRequest.Player>) {
        hostPlayer = players[0] // 승자 중 앞에 있는 사람이 호스트
        callback?.gameResult(players, 10, hostPlayer)

        players.forEach {
            scoreBoard[it] = scoreBoard.getOrDefault(it, 0) + 10
        }
    }

    private fun processRetire() {
        timeoutCheckList.forEach {
            playersList.remove(it)

            if (it.ip == hostPlayer?.ip &&
                it.name == hostPlayer?.name
            ) {
                if (playersList.isNotEmpty()) {
                    hostPlayer = playersList[0]
                    callback?.changeHost(playersList[0])
                } else {
                    hostPlayer = null
                }
            }
        }

        checkUserLog()

        callback?.ready2Play(hostPlayer)
    }

    override fun select(player: GRequest.Player, select: GRequest.Select) {
        timeoutCheckList.remove(player)
        gameBoard[player] = select
    }

    override fun timeoutCheck(player: GRequest.Player) {
        timeoutCheckList.remove(player)
    }

    override fun setGameCallback(callback: RspGameCallback) {
        this@RspGameServiceImpl.callback = callback
    }

    override fun gameRank(): List<Pair<GRequest.Player, Int>> {

        return scoreBoard.toList().sortedWith(compareBy {it.second})
    }

    override fun isHost(request: Gamer): Welecom.ClientType =
        if (hostPlayer?.ip == request.ip && hostPlayer?.name == request.name) Welecom.ClientType.HOST
        else Welecom.ClientType.GUEST

    override fun isPlaying(): Welecom.Status =
        if (isPlaying) Welecom.Status.WAIT
        else Welecom.Status.READY

}