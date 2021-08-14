package me.heaton.tennis

import kotlin.math.abs

class TennisGame(private val firstPlayer: String, private val secondPlayer: String) {
    private var scoreEvents = mutableListOf<String>()
    private val scoreHistory = mutableListOf(score())

    private fun firstPlayerPoint(): Int = points().first

    private fun secondPlayerPoint(): Int = points().second

    fun score(): String =
        when {
            afterDeuce() && firstPlayerPoint() == secondPlayerPoint() -> "deuce"
            afterDeuce() && abs(firstPlayerPoint() - secondPlayerPoint()) == 1 -> "advantage ${betterPlayer()}"
            firstPlayerPoint() > 3 || secondPlayerPoint() > 3 -> "${betterPlayer()} wins"
            else -> "${scoreOf(firstPlayerPoint())}, ${scoreOf(secondPlayerPoint())}"
        }

    private fun betterPlayer(): String = if (firstPlayerPoint() > secondPlayerPoint()) firstPlayer else secondPlayer

    private fun afterDeuce() = firstPlayerPoint() > 2 && secondPlayerPoint() > 2

    private fun scoreOf(point: Int) = when (point) {
        0 -> "love"
        1 -> "15"
        2 -> "30"
        else -> "40"
    }

    fun firstPlayerScores() {
        if ("wins" in score()) throw GameEndedException()
        scoreEvents.add(firstPlayer)
        scoreHistory.add(score())
    }

    private fun points() = scoreEvents.fold(Pair(0, 0), ::addScoreForPlayer)

    private fun addScoreForPlayer(previousScore: Pair<Int, Int>, player: String) = with(previousScore) {
        if (player == firstPlayer) Pair(first + 1, second) else Pair(first, second + 1)
    }

    fun secondPlayerScores() {
        if ("wins" in score()) throw GameEndedException()
        scoreEvents.add(secondPlayer)
        scoreHistory.add(score())
    }

    fun review(): String = """
        |$firstPlayer, $secondPlayer
        |${scoreHistory.joinToString("\n")}
    """.trimMargin()

}
