import java.io.File
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

private val realInput = File("inputs/Day13Input.txt").readLines()
private val testInput = File("inputs/Day13TestInput.txt").readLines()

class Day13(val input: List<String>) {

    val initialDots: List<Pair<Int, Int>>
    val initialSteps: List<Pair<Char, Int>>

    init {
        var line = 0
        val dots = ArrayList<Pair<Int, Int>>()
        val steps = ArrayList<Pair<Char, Int>>()
        while (input[line].isNotEmpty()) {
            input[line++].split(",").let { dots.add(Pair(it.first().toInt(), it.last().toInt())) }
        }
        line++
        initialDots = dots
        for (i in line until input.size) {
            input[i].drop(11).split("=").let {
                steps.add(Pair(it.first().first(), it.last().toInt()))
            }
        }
        initialSteps = steps
    }

    fun solve(firstStepOnly: Boolean): Int {
        val page = initialDots.toMutableList()
        initialSteps.let { if (!firstStepOnly) initialSteps else initialSteps.take(1) }.forEach { step ->
            if (step.first == 'y') {
                val foldY = step.second
                //If the point.second is > fold, subtract (distance from fold * 2)
                page.replaceAll {
                    it.takeIf { it.y > foldY }?.let {
                        Pair(it.x, it.y - (it.y - foldY) * 2)
                    } ?: it
                }
            } else {
                val foldX = step.second
                page.replaceAll {
                    it.takeIf { it.x > foldX }?.let {
                        Pair(it.x - (it.x - foldX) * 2, it.y)
                    } ?: it
                }
            }
        }
        printPage(page)
        return page.toSet().size
    }

    private fun printPage(page: List<Pair<Int, Int>>) {
        val pageSet = page.toSet()
        val maxX = pageSet.maxByOrNull { it.x }!!.x
        val maxY = pageSet.maxByOrNull { it.y }!!.y
        for (y in 0..maxY) {
            (0..maxX).map { x ->
                if (Pair(x, y) in pageSet) '#' else ' '
            }.let { println(it.joinToString("")) }
        }
    }

}

private fun test() {
    Day13(testInput).apply {
        assertEquals(16, solve(false))
    }
}

fun main() {
    test()
    Day13(realInput).apply {
        println(solve(true))
        println(solve(false))
    }
}
