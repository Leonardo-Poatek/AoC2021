import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.test.assertEquals

private val realInput = File("inputs/Day10Input.txt").readLines()
private val testInput = File("inputs/Day10TestInput.txt").readLines()

class Day10 {

    fun solve1(input: List<String>): Int {
        val results = input.mapNotNull {
            validateSequence(it).first?.second
        }
        return getSolve1Points(results)
    }

    private fun getSolve1Points(errors: List<Char>) = errors.sumBy {
        when(it) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            else -> 25137
        }
    }

    fun solve2(input: List<String>): Long {
        val analysis = input.mapNotNull {
            validateSequence(it).second?.takeIf { it.isNotEmpty() }
        }
        val results = analysis.map(::getSolve2Points)
        return results.sorted()[results.size/2].toLong()
    }

    private fun getSolve2Points(completion: List<Char>) = completion.fold(0.0) { acc, char ->
        val point = when(char) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            else -> 4
        }
        acc * 5.0 + point.toDouble()
    }

    /**
     * Returns a pair with the expected value, and the found value. And also the remaining symbols to make the sequence valid.
     */
    fun validateSequence(input: String): Pair<Pair<Char, Char>?, List<Char>?> {
        val expectedClosing = ArrayDeque<Char>()

        input.forEach { character ->
            when(character) {
                '{' -> expectedClosing.push('}')
                '[' -> expectedClosing.push(']')
                '(' -> expectedClosing.push(')')
                '<' -> expectedClosing.push('>')
                '}', ']', ')', '>' -> expectedClosing.pop().let { if(it != character) return Pair(Pair(it, character), null) }
            }
        }

        return Pair(null, expectedClosing.toList())
    }

}

private fun test() {
    Day10().apply {
        assertEquals(26397, solve1(testInput))
        assertEquals(288957, solve2(testInput))
    }
}

fun main() {
    test()
    Day10().apply {
        println(solve1(realInput))
        println(solve2(realInput))
    }
}
