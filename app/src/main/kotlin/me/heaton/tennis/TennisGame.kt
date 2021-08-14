package me.heaton.tennis

import kotlin.math.abs

class TennisGame(private val firstPlayer: String, private val secondPlayer: String) {
    private var scoreEvents = mutableListOf<String>()
    private val scoreHistory = mutableListOf(score())

    private fun firstPlayerPoint(): Int = points(scoreEvents).first

    private fun secondPlayerPoint(): Int = points(scoreEvents).second

    fun score(): String = score(Pair(firstPlayerPoint(), secondPlayerPoint()))

    private fun score(points: Pair<Int, Int>) = with(points) {
        when {
            afterDeuce(points) && first == second -> "deuce"
            afterDeuce(points) && abs(first - second) == 1 -> "advantage ${betterPlayer(points)}"
            first > 3 || second > 3 -> "${betterPlayer(points)} wins"
            else -> "${scoreOf(first)}, ${scoreOf(second)}"
        }
    }

    private fun betterPlayer(points: Pair<Int, Int>): String =
        if (points.first > points.second) firstPlayer else secondPlayer

    private fun afterDeuce(points: Pair<Int, Int>) = points.first > 2 && points.second > 2

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

    private fun points(events: List<String>) = events.fold(Pair(0, 0), ::addScoreForPlayer)

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
