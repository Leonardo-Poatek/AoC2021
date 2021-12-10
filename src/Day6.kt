import java.io.File
import kotlin.test.assertEquals

class Day6 {

    private val input = File("inputs/Day6Input.txt").readLines().first().split(",").map { it.toInt() }

    private val memory = HashMap<Pair<Int, Int>, Double>()

    private fun getChildrenDays(counter: Int, currentDay: Int, lastDay: Int): Sequence<Int> {
        if (counter >= lastDay - currentDay) return emptySequence()
        //This +1 here is because you dont create a new child until resetting the counter
        return generateSequence(currentDay + counter + 1) { (it + 7).takeIf { it <= lastDay } }
    }

    fun solve(numberOfDays: Int) = input.size + input.sumByDouble { counter ->
        solveRec(counter, 0, numberOfDays)
    }

    private fun solveRec(counter: Int, initialDay: Int, lastDay: Int): Double {
        val key = Pair(counter, lastDay - initialDay)
        memory[key]?.let { return it }

        val children = getChildrenDays(counter, initialDay, lastDay)
        if (children.count() == 0) return 0.0

        val count = children.count() + children.sumByDouble { childDay ->
            solveRec(8, childDay, lastDay)
        }

        memory[key] = count
        return count
    }

}

fun main() {
    Day6().apply {
        println(solve(80).toLong())
        println(solve(256).toLong())
    }

    

}
