import java.io.File
import kotlin.test.assertEquals

private val fakeInput = File("inputs/Day8FakeInput.txt").readLines()
private val realInput = File("inputs/Day8Input.txt").readLines()

private fun parseInput(lines: List<String>) = lines.map {
    it.split(" | ").let { it.first().split(" ") to it.last().split(" ") }
}

class Day8 {

    fun solve1(input: List<Pair<List<String>, List<String>>>): Int {
        val lengths = setOf(2, 4, 3, 7)
        return input.sumBy { it.second.count { it.length in lengths } }
    }


    /*
 0000
5    1
5    1
 6666
4    2
4    2
 3333
     */

    fun MutableList<Set<Char>>.removeFirst(x: (Set<Char>) -> Boolean): Set<Char> {
        val index = indexOfFirst { x.invoke(it) }
        return removeAt(index)
    }

    fun findNumbers(input: List<String>): Map<Int, CharArray> {
        val numbers = input.map { it.toSet() }.toMutableList()
        val foundNumbers = HashMap<Int, Set<Char>>()
        foundNumbers[1] = numbers.removeFirst { it.size == 2 }
        foundNumbers[4] = numbers.removeFirst { it.size == 4 }
        foundNumbers[7] = numbers.removeFirst { it.size == 3 }
        foundNumbers[8] = numbers.removeFirst { it.size == 7 }
        foundNumbers[2] = numbers.removeFirst { it.size == 5 && (it - foundNumbers[4]!!).size == 3 } //5
        foundNumbers[3] = numbers.removeFirst { it.size == 5 && (it - foundNumbers[1]!!).size == 3 } //5
        foundNumbers[5] = numbers.removeFirst { it.size == 5 && (it - foundNumbers[1]!!).size == 4 } //5
        foundNumbers[9] = numbers.removeFirst { (it - foundNumbers[4]!!).size == 2 } //6
        foundNumbers[6] = numbers.removeFirst { (it - foundNumbers[7]!!).size == 4 } //6
        foundNumbers[0] = numbers.last()                                             //6
        return foundNumbers.mapValues { it.value.sorted().toCharArray() }
    }

    fun solve2(input: List<Pair<List<String>, List<String>>>): Int {

        val sum = input.sumBy { line ->

            val foundNumbers = findNumbers(line.first)

            line.second.map { outputNumber ->
                foundNumbers.entries.first {
                    it.value.contentEquals(outputNumber.toCharArray().sortedArray())
                }.key
            }.joinToString("").toInt()

        }

        return sum
    }

}


fun main() {

    Day8().apply {
        println(solve1(parseInput(realInput)))
        println(solve2(parseInput(realInput)))
    }

}
