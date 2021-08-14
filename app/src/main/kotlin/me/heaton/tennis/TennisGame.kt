package me.heaton.tennis

import kotlin.math.abs

typealias Points = Pair<Int, Int>

class TennisGame(private val firstPlayer: String, private val secondPlayer: String) {
    private val scoreEvents = mutableListOf<String>()

    fun firstPlayerScores() = playerScores(firstPlayer)

    fun secondPlayerScores() = playerScores(secondPlayer)

    private fun playerScores(player: String) {
        if ("wins" in score()) throw GameEndedException()
        scoreEvents.add(player)
    }

    fun score(): String = score(points(scoreEvents))

    private fun points(events: List<String>) = with(events) {
        Pair(count { it == firstPlayer }, count { it == secondPlayer })
    }

    private fun score(points: Points) = with(points) {
        when {
            afterDeuce() && first == second -> "deuce"
            afterDeuce() && abs(first - second) == 1 -> "advantage ${betterPlayer()}"
            first > 3 || second > 3 -> "${betterPlayer()} wins"
            else -> "${scoreOf(first)}, ${scoreOf(second)}"
        }
    }

    private fun Points.afterDeuce() = first > 2 && second > 2

    private fun Points.betterPlayer(): String = if (first > second) firstPlayer else secondPlayer

    private fun scoreOf(point: Int) = when (point) {
        0 -> "love"
        1 -> "15"
        2 -> "30"
        else -> "40"
    }

    fun review(): String = """
        |$firstPlayer, $secondPlayer
        |${scoreHistory().joinToString("\n")}
    """.trimMargin()

    private fun scoreHistory() = scoreEvents.histories().map(::points).map(::score)

    private fun <T> List<T>.histories() = fold(listOf(emptyList<T>())) { acc, event ->
        acc.plusElement(acc.last() + event)
    }

}
