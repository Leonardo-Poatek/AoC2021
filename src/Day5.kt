import java.io.File
import java.lang.Double.max


val Pair<Int, Int>.x: Int
    get() = this.first

val Pair<Int, Int>.y: Int
    get() = this.second

class Day5 {

    private val lineSegments = File("inputs/Day5Input.txt").readLines().map {
        it.split(" -> ").let { linePair ->
            Pair(splitToPair(linePair[0]), splitToPair(linePair[1]))
        }
    }

    private fun splitToPair(strPair: String): Pair<Int, Int> {
        return strPair.split(",").let { Pair(it[0].toInt(), it[1].toInt()) }
    }

    private fun generateIntermediatePairs(p1: Pair<Int, Int>, p2: Pair<Int, Int>): List<Pair<Int, Int>> {

        val (first, second) = if (p1.x < p2.x) Pair(p1, p2) else Pair(p2, p1)

        var currentX = first.x
        var currentY = first.y
        val isDecY = first.y > second.y //We know the X value always increase, but we have to check the Y value.

        val pairs = mutableListOf<Pair<Int, Int>>()

        while (currentX != second.x || currentY != second.y) {
            pairs.add(Pair(currentX, currentY))
            currentX = (currentX + 1).coerceAtMost(second.x)
            currentY = if (isDecY) (currentY - 1).coerceAtLeast(second.y) else (currentY + 1).coerceAtMost(second.y)
        }

        //Add the last pair
        pairs.add(Pair(currentX, currentY))

        return pairs
    }

    fun solve1(): Int {
        val filteredLines = lineSegments.filter { it.first.x == it.second.x || it.first.y == it.second.y }

        val board = HashMap<String, Int>()

        filteredLines.forEach { pair ->
            generateIntermediatePairs(pair.first, pair.second).forEach {
                val key = it.toString()
                board[key] = board[key]?.plus(1) ?: 1
            }
        }

        return board.values.count { it > 1 }
    }

    fun solve2(): Int {
        val board = HashMap<String, Int>()

        lineSegments.forEach { pair ->
            generateIntermediatePairs(pair.first, pair.second).forEach {
                val key = it.toString()
                board[key] = board[key]?.plus(1) ?: 1
            }
        }

        return board.values.count { it > 1 }
    }

}

fun main() {
    Day5().apply {
        println(solve1())
        println(solve2())
    }
}
