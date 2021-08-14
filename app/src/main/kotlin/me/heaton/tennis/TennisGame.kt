package me.heaton.tennis

import kotlin.math.abs

class TennisGame(private val firstPlayer: String, private val secondPlayer: String) {
    private var scoreEvents = mutableListOf<String>()

    fun score(): String = score(points(scoreEvents))

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
    }

    private fun points(events: List<String>) = events.fold(Pair(0, 0), ::addScoreForPlayer)

    private fun addScoreForPlayer(previousScore: Pair<Int, Int>, player: String) = with(previousScore) {
        if (player == firstPlayer) Pair(first + 1, second) else Pair(first, second + 1)
    }

    fun secondPlayerScores() {
        if ("wins" in score()) throw GameEndedException()
        scoreEvents.add(secondPlayer)
    }

    fun review(): String = """
        |$firstPlayer, $secondPlayer
        |${scoreHistory().joinToString("\n")}
    """.trimMargin()

    private fun scoreHistory() = scoreEvents.fold(listOf(emptyList<String>())) { acc, event ->
        acc.plusElement(acc.last() + event)
    }.map(::points).map(::score)

}
