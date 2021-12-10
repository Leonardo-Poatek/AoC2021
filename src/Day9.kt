import java.io.File
import kotlin.test.assertEquals

private val testInput = File("inputs/Day9TestInput.txt").readLines()
private val realInput = File("inputs/Day9Input.txt").readLines()

private fun parseInput(lines: List<String>) = lines.map { it.toList().map { Character.getNumericValue(it) } }

class Day9 {

    fun solve1(input: List<List<Int>>): Int {
        var lowPointsCount = 0
        val height = input.size
        val width = input[0].size
        for (y in 0 until height) {
            for (x in 0 until width) {
                val number = input[y][x]
                if ((x == 0 || input[y][x - 1] > number) //Left
                    && (x == width - 1 || input[y][x + 1] > number) //Right
                    && (y == 0 || input[y - 1][x] > number) //Top
                    && (y == height - 1 || input[y + 1][x] > number) //Bottom
                ) {
                    lowPointsCount += number + 1
                }
            }
        }
        return lowPointsCount
    }

    var height = 0
    var width = 0

    fun solve2(input: List<List<Int>>): Int {

        val visitedPoints = HashSet<String>()
        val basins = ArrayList<List<Int>>()

        height = input.size
        width = input[0].size
        for (y in 0 until height) {
            for (x in 0 until width) {
                val number = input[y][x]
                if ("$x,$y" !in visitedPoints && number != 9) {
                    basins.add(visit(input, x, y, visitedPoints))
                }
            }
        }

        return basins.sortedByDescending { it.size }.take(3).let {
            it[0].size * it[1].size * it[2].size
        }
    }

    private fun visit(ground: List<List<Int>>, x: Int, y: Int, visited: HashSet<String>): List<Int> {
        if ("$x,$y" in visited || ground[y][x] == 9) return emptyList()

        visited.add("$x,$y")

        val values = ArrayList<Int>()
        values.add(ground[y][x])

        if (x > 0) values.addAll(visit(ground, x - 1, y, visited))
        if (x < width - 1) values.addAll(visit(ground, x + 1, y, visited))

        if (y > 0) values.addAll(visit(ground, x, y - 1, visited))
        if (y < height - 1) values.addAll(visit(ground, x, y + 1, visited))

        return values
    }

}

private fun test() {
    Day9().apply {
        assertEquals(15, solve1(parseInput(testInput)))
        assertEquals(1134, solve2(parseInput(testInput)))
        assertEquals(216, solve2(listOf(
            listOf(9,9,9,9,9,9,9,9),
            listOf(9,2,3,5,6,7,8,9),
            listOf(9,9,9,9,9,9,9,9),
            listOf(9,1,2,3,4,5,6,9),
            listOf(9,9,9,9,9,9,9,9),
            listOf(9,1,2,3,4,5,6,9),
            listOf(9,9,9,9,9,9,9,9)
        )))
    }
}

fun main() {
    test()
    Day9().apply {
        println(solve1(parseInput(realInput)))
        println(solve2(parseInput(realInput)))
    }
}
