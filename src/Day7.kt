import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.test.assertEquals

private val input: List<Int> = File("inputs/Day7Input.txt").readLines().first().split(",").map { it.toInt() }

class Day7 {

    private fun mediana(list: List<Int>): Float {
        val sorted = list.sorted()
        val size = sorted.size
        return if (size % 2 != 0) {
            sorted[size / 2].toFloat()
        } else {
            (sorted[size / 2] + sorted[size / 2 - 1]) / 2f
        }
    }

    fun solve1(input: List<Int>): Int {
        val mediana = mediana(input).roundToInt()
        return input.sumBy { abs(it - mediana) }
    }

    private fun calculatePASum(start:Int, n:Int): Int {
        return n * (start + n)/2
    }

    fun solve2(input: List<Int>): Int {
         val a = (input.minOrNull()!! .. input.maxOrNull()!!).map { position ->
            input.sumBy { calculatePASum(1, abs(it - position)) }
        }
        return a.minOrNull()!!
    }

}

private fun test() {
    val day7 = Day7()
    assertEquals(37, day7.solve1(listOf(16,1,2,0,4,2,7,1,2,14)))
    assertEquals(10, day7.solve1(listOf(0, 10)))
    assertEquals(141, day7.solve1(listOf(0, 10, 51, 100)))
    assertEquals(100_000, day7.solve1(listOf(0,0,0,0,0,0,0,0,100_000)))
}

fun main() {

    test()

    Day7().apply {
        println(solve1(input))
        println(solve2(input))
    }

}
