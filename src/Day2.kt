import java.io.File

class Day2 {

    data class Command(val command: String, val ammount: Int)

    val input = File("inputs/Day2Input.txt").readLines().map {
        it.split(" ").let { Command(it[0], it[1].toInt()) }
    }

    fun solve1(): Int {

        var depth = 0
        var hor = 0

        input.forEach {
            when(it.command) {
                "forward" -> hor += it.ammount
                "down" -> depth += it.ammount
                "up" -> depth -= it.ammount
            }
        }

        return depth * hor
    }

    fun solve2(): Int {
        var aim = 0
        var depth = 0
        var hor = 0

        input.forEach {
            when(it.command) {
                "down" -> aim += it.ammount
                "up" -> aim -= it.ammount
                "forward" -> {
                    hor += it.ammount
                    depth += aim * it.ammount
                }
            }
        }

        return depth * hor
    }

}

fun main() {
    Day2().apply {
        println(solve1())
        println(solve2())
    }
}
