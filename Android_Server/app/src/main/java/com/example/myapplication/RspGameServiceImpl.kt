package com.example.myapplication

import android.text.TextUtils
import google.example.GRequest
import google.example.Gamer
import google.example.Welecom
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timer
import kotlin.math.log

class RspGameServiceImpl : RspGameService {

    val scoreBoard: HashMap<String, Int> = HashMap()
    private val playersList = HashMap<String, String>()
    private val timeoutCheckList = HashMap<String, String>()

    var hostPlayer: Pair<String, String>? = null

    val gameBoard: HashMap<String, GRequest.Select> = HashMap()

    var callback: RspGameCallback? = null

    var time = 0
    var timerTask: Timer? = null
    var isPlaying = false

    override fun joinPlayer(gamer: Gamer) {

        if (!playersList.containsKey(gamer.ip)) {
            playersList[gamer.ip] = gamer.name
        }

        if (playersList.size == 1) {
            hostPlayer = Pair(gamer.ip, gamer.name)
            println("$hostPlayer is host")
        }
    }

    override fun leftPlayer(player: GRequest.Player?) {
        player?.let {
            playersList.remove(player.ip)

            if (player.ip == hostPlayer?.first &&
                player.name == hostPlayer?.second
            ) {
                if (playersList.isNotEmpty()) {
                    hostPlayer = Pair(it.ip, it.name)
                    callback?.changeHost(hostPlayer!!)
                } else {
                    hostPlayer = null
                }
            }
        }
    }

    override fun startHost(player: GRequest.Player): Boolean {
        return if (TextUtils.equals(player.ip,hostPlayer?.first) &&
            TextUtils.equals(player.name, hostPlayer?.second)
        ) {
            println("startHost")
            gameBoard.clear()
            playersList.forEach {
                gameBoard[it.key] = GRequest.Select.NONE
            }

            timeoutCheckList.clear()
            timeoutCheckList.putAll(playersList)

            time = 0
            callback?.startGame(playersList, hostPlayer)
            isPlaying = true
            startTimer()
            true
        } else {
            println("startHost : fail ")
            false
        }
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {
            time++


            if (time == 20) {
                callback?.gameTimeout(hostPlayer)
            }

            if (time == 20 + 5) {
                timerTask?.cancel()
                processGame()
            }
        }
    }

    private fun processGame() {
        var scissors = ArrayList<Pair<String, String>>()
        var rocks = ArrayList<Pair<String, String>>()
        var paper = ArrayList<Pair<String, String>>()

        gameBoard.entries.forEach { it ->
            println("${it.key} : ${it.value}")
            when (gameBoard[it.key]) {
                GRequest.Select.NONE -> {
                    callback?.leavePlayer(Pair(it.key, playersList[it.key]))
                    playersList.remove(it.key)
                }
                GRequest.Select.ROCK -> rocks.add(Pair(it.key, playersList[it.key]!!))
                GRequest.Select.PAPER -> paper.add(Pair(it.key, playersList[it.key]!!))
                GRequest.Select.SCISSOR -> scissors.add(Pair(it.key, playersList[it.key]!!))
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
        callback?.ready2Play(hostPlayer)
    }

    private fun gameResult(players: ArrayList<Pair<String, String>>) {
        hostPlayer = players[0] // 승자 중 앞에 있는 사람이 호스트
        callback?.gameResult(players, 10, hostPlayer)
        callback?.changeHost(hostPlayer!!)

        players.forEach {
            scoreBoard[it.second] = scoreBoard.getOrDefault(it.second, 0) + 10
        }
    }

    private fun processRetire() {
        timeoutCheckList.forEach {
            playersList.remove(it.key)
            callback?.leavePlayer(Pair(it.key, it.value))

            if (it.key == hostPlayer?.first &&
                it.value == hostPlayer?.second
            ) {
                if (playersList.isNotEmpty()) {
                    hostPlayer = Pair(playersList.keys.first(), playersList[playersList.keys.first()]!!)
                    callback?.changeHost(hostPlayer!!)
                } else {
                    hostPlayer = null
                }
            }
        }

        callback?.ready2Play(hostPlayer)
    }

    override fun select(player: GRequest.Player, select: GRequest.Select) {
        gameBoard[player.ip] = select
        println("${player.ip} : $select")
    }

    override fun timeoutCheck(player: GRequest.Player) {
        timeoutCheckList.remove(player.ip)
    }

    override fun setGameCallback(callback: RspGameCallback) {
        this@RspGameServiceImpl.callback = callback
    }

    override fun gameRank(): List<Pair<String, Int>> {

        return scoreBoard.toList().sortedWith(compareBy {it.second})
    }

    override fun isHost(request: String): Welecom.ClientType =
        if (hostPlayer?.first == request) Welecom.ClientType.HOST
        else Welecom.ClientType.GUEST

    override fun isPlaying(): Welecom.Status =
        if (isPlaying) Welecom.Status.WAIT
        else Welecom.Status.READY

}