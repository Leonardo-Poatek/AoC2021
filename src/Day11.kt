import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.test.assertEquals

private val realInput: List<MutableList<Int>>
    get() {
        return File("inputs/Day11Input.txt").readLines()
            .map { it.toCharArray().map { Character.getNumericValue(it) }.toMutableList() }
    }

private val testInput: List<MutableList<Int>>
    get() {
        return File("inputs/Day11TestInput.txt").readLines()
            .map { it.toCharArray().map { Character.getNumericValue(it) }.toMutableList() }
    }

class Day11 {

    private fun computeCycle(input: List<MutableList<Int>>): Int {

        var flashes = 0

        /**
         * First, increment everything
         * Then, expand the flashes
         */

        val revisit = ArrayDeque<Pair<Int, Int>>()

        for (y in input.indices) {
            for (x in input.indices) {
                input[y][x]++
                if (input[y][x] > 9) {
                    revisit.push(Pair(x, y))
                }
            }
        }

        val flashed = HashSet<Pair<Int, Int>>()

        while (revisit.isNotEmpty()) {
            val pos = revisit.pop()

            if (pos in flashed || pos.first !in 0 until 10 || pos.second !in 0 until 10) continue

            input[pos.second][pos.first]++

            if (input[pos.second][pos.first] > 9) {
                flashed.add(pos)
                input[pos.second][pos.first] = 0
                flashes++

                revisit.add(Pair(pos.first - 1, pos.second - 1))
                revisit.add(Pair(pos.first - 1, pos.second))
                revisit.add(Pair(pos.first - 1, pos.second + 1))

                revisit.add(Pair(pos.first, pos.second - 1))
                revisit.add(Pair(pos.first, pos.second + 1))

                revisit.add(Pair(pos.first + 1, pos.second - 1))
                revisit.add(Pair(pos.first + 1, pos.second))
                revisit.add(Pair(pos.first + 1, pos.second + 1))
            }
        }

        return flashes
    }

    fun solve1(input: List<MutableList<Int>>, Cycles: Int): Int {
        var flashes = 0
        repeat(Cycles) {
            flashes += computeCycle(input)
        }
        printBoard(input)
        return flashes
    }

    fun solve2(input: List<MutableList<Int>>): Int {
        var i = 0
        while(true) {
            if(computeCycle(input) == 100) return i + 1
            i++
        }
    }

    private fun printBoard(input: List<MutableList<Int>>) {
        input.forEach {
            println(it)
        }
    }

}

private fun test() {
    Day11().apply {
        assertEquals(204, solve1(testInput, 10))
        assertEquals(1656, solve1(testInput, 100))
        assertEquals(195, solve2(testInput))
    }
}

fun main() {
    test()
    Day11().apply {
        println(solve1(realInput, 100))
        println(solve2(realInput))
    }
}
