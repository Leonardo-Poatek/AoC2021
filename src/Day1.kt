import java.io.File

class Day1 {

    val input = File("inputs/Day1Input.txt").useLines { it.map { it.toInt() }.toList() }

    private fun generateWindows(): List<Int> {
        return input.windowed(3, 1) { it.sum() }
    }

    fun solve1(): Int {
        return solve(input)
    }

    fun solve2(): Int {
        return solve(generateWindows())
    }

    private fun solve(input: List<Int>): Int {

        var count = 0

        for(i in 1 until input.size) {
            if (input[i - 1] < input[i]) {
                count++
            }
        }

        return count

    }

}

fun main() {
    Day1().apply {
        println(solve1())
        println(solve2())
    }
}
