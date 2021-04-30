package com.example.myapplication

import google.example.GRequest
import google.example.Gamer
import google.example.Welecom

interface RspGameService {
    fun joinPlayer(gamer: Gamer)
    fun leftPlayer(player: GRequest.Player?)
    fun startHost(player: GRequest.Player):Boolean
    fun select(player: GRequest.Player, select: GRequest.Select)
    fun timeoutCheck(player: GRequest.Player)
    fun setGameCallback(callback: RspGameCallback)
    fun gameRank():List<Pair<GRequest.Player, Int>>// 유저, 점수, 순위
    fun isHost(request: Gamer): Welecom.ClientType
    fun isPlaying(): Welecom.Status
}

interface RspGameCallback {
    fun gameResult(player: List<GRequest.Player>, point: Int = 10, hostPlayer: GRequest.Player?)
    fun gameTimeout(hostPlayer: GRequest.Player?)
    fun startGame(player: List<GRequest.Player>, hostPlayer: GRequest.Player?)
    fun gameDraw(hostPlayer: GRequest.Player?)
    fun leavePlayer(key: GRequest.Player?)
    fun ready2Play(hostPlayer: GRequest.Player?)
    fun changeHost(hostPlayer: GRequest.Player)
}