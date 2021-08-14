package me.heaton.tennis

import kotlin.math.abs

class TennisGame(private val firstPlayer: String, private val secondPlayer: String) {
    private var _firstPlayerPoint = 0
    private var _secondPlayerPoint = 0
    private val scoreHistory = mutableListOf(score())

    private fun firstPlayerPoint() = _firstPlayerPoint
    private fun secondPlayerPoint() = _secondPlayerPoint

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
        _firstPlayerPoint++
        scoreHistory.add(score())
    }

    fun secondPlayerScores() {
        if ("wins" in score()) throw GameEndedException()
        _secondPlayerPoint++
        scoreHistory.add(score())
    }

    fun review(): String = """
        |$firstPlayer, $secondPlayer
        |${scoreHistory.joinToString("\n")}
    """.trimMargin()

}
