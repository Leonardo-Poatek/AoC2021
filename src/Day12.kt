import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.test.assertEquals

private val realInput: List<String>
    get() {
        return File("inputs/Day12Input.txt").readLines()
    }

class Day12 {

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

    class Cave(val name: String) {
        private var _neighbours: HashMap<String, Cave> = HashMap()

        val isBigCave: Boolean = name.all { it.isUpperCase() }
        val isSmallCave: Boolean = !isBigCave

        val neighbors: Map<String, Cave>
            get() = _neighbours

        fun addNeighbour(cave: Cave) {
            if (!_neighbours.containsKey(cave.name)) {
                _neighbours[cave.name] = cave
            }
        }
    }

    fun parseGraph(lines: List<String>): HashMap<String, Cave> {
        val nodes = HashMap<String, Cave>()

        lines.forEach {
            val split = it.split("-")
            val node0 = nodes.getOrPut(split[0]) { Cave(split[0]) }
            val node1 = nodes.getOrPut(split[1]) { Cave(split[1]) }
            node0.addNeighbour(node1)
            node1.addNeighbour(node0)
        }

        return nodes
    }

    fun solve1(input: List<String>): Int {
        val caves = parseGraph(input)
        val start = caves["start"]!!
        return safeTravels(start, ArrayList()).size
    }

    fun <T> MutableList<T>.copyAndAdd(item: T) = this.toMutableList().apply { add(item) }

    private fun safeTravels(currentCave: Cave, path: MutableList<Cave>): List<List<Cave>> {

        val newPath = path.copyAndAdd(currentCave)

        if(currentCave.name == "end") return listOf(newPath)

        val otherPaths = ArrayList<List<Cave>>()

        currentCave.neighbors.values.forEach { nextCave ->
            if (nextCave.isBigCave || (nextCave.isSmallCave && nextCave !in path)) {
                otherPaths.addAll(safeTravels(nextCave, newPath))
            }
        }

        return otherPaths
    }


    fun solve2(input: List<String>): Int {
        val caves = parseGraph(input)
        val start = caves["start"]!!
        val paths = safeTravels2(start, ArrayList())
        paths.forEach {
            val repeated = it.groupBy { it }.count { it.value.size > 1 && it.key.isSmallCave }
            if(repeated > 1) {
                println(it.map { it.name }.joinToString())
            }
        }
        return paths.size
    }

    private fun safeTravels2(currentCave: Cave, path: MutableList<Cave>): List<List<Cave>> {

        val newPath = path.copyAndAdd(currentCave)

        if(currentCave.name == "end") return listOf(newPath)

        val otherPaths = ArrayList<List<Cave>>()

        val visitedCount = newPath.groupingBy { it }.eachCount()
        val visitedTwice = visitedCount.any { it.key.isSmallCave && it.value == 2 }

        currentCave.neighbors.values.forEach { nextCave ->
            val nextCount = visitedCount[nextCave] ?: 0
            if (nextCave.isBigCave || (nextCave.isSmallCave && (nextCount == 0) || (nextCount == 1 && !visitedTwice)) && nextCave.name != "start") {
                otherPaths.addAll(safeTravels2(nextCave, newPath))
            }
        }

        return otherPaths
    }

}

private fun test() {
    Day12().apply {

        val input1 = listOf(
            "dc-end",
            "HN-start",
            "start-kj",
            "dc-start",
            "dc-HN",
            "LN-dc",
            "HN-end",
            "kj-sa",
            "kj-HN",
            "kj-dc"
        )

        assertEquals(19, solve1(input1))

        val input2 = listOf(
            "fs-end",
            "he-DX",
            "fs-he",
            "start-DX",
            "pj-DX",
            "end-zg",
            "zg-sl",
            "zg-pj",
            "pj-he",
            "RW-he",
            "fs-DX",
            "pj-RW",
            "zg-RW",
            "start-pj",
            "he-WI",
            "zg-he",
            "pj-fs",
            "start-RW"
        )

        assertEquals(226, solve1(input2))

        val input3 = listOf(
            "start-A",
            "start-b",
            "A-c",
            "A-b",
            "b-d",
            "A-end",
            "b-end"
        )

        assertEquals(36, solve2(input3))

        assertEquals(3410, solve1(realInput))
        assertEquals(98796, solve2(realInput))
    }
}

fun main() {
    test()
    Day12().apply {
        println(solve1(realInput))
        println(solve2(realInput))
    }
}
