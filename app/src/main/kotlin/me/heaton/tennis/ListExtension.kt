package me.heaton.tennis

/**
 * List(1, 2, 3).histories() =>
 * List(
 *   List(),
 *   List(1),
 *   List(1, 2),
 *   List(1, 2, 3)
 * )
 */
fun <T> List<T>.histories() = fold(listOf(emptyList<T>())) { acc, e ->
    acc.plusElement(acc.last() + e)
}