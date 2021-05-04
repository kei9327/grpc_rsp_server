package example

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
    fun gameRank(): List<Pair<String, Int>>// 유저, 점수, 순위
    fun isHost(request: String): Welecom.ClientType
    fun isPlaying(): Welecom.Status
}

interface RspGameCallback {
    fun gameResult(player: ArrayList<Pair<String, String>>, point: Int = 10, hostPlayer: Pair<String, String>?)
    fun gameTimeout(hostPlayer: Pair<String, String>?)
    fun startGame(player: HashMap<String, String>, hostPlayer: Pair<String, String>?)
    fun gameDraw(hostPlayer: Pair<String, String>?)
    fun leavePlayer(key: Pair<String, String?>)
    fun ready2Play(hostPlayer: Pair<String, String>?)
    fun changeHost(hostPlayer: Pair<String, String>)
}