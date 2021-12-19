import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.test.assertEquals

private val realInput = File("inputs/Day15Input.txt").readLines()
private val testInput = File("inputs/Day15TestInput.txt").readLines()

class Node<T>(val value: T) {

    private var _edges: HashMap<Node<T>, Int> = HashMap()
    val edges: Map<Node<T>, Int>
        get() = _edges

    fun addEdge(node: Node<T>, weight: Int) {
        _edges[node] = weight
    }
}

class Edge(val adj: Int, val weight: Int)

@ExperimentalStdlibApi
class Day15(val input: List<String>) {

    val graph = HashSet<Node<Int>>()

    val mapHeight: Int = input.size
    val mapWidth: Int = input.first().length
    val nodeCount = mapHeight * mapWidth

    fun djkstra(start: Int = 0, end: Int = mapHeight * mapWidth - 1): Int {

        var distances = IntArray(nodeCount) { Int.MAX_VALUE }
        var visited = BooleanArray(nodeCount) { false }

        val queue: Queue<Pair<Int, Int>> = ArrayDeque()

        distances[start] = 0

        queue.add(Pair(distances[start], start))

        while (queue.isNotEmpty()) {

            val p = queue.poll()
            val node = p.second

            if (!visited[node]) {

                visited[node] = true

                //Go through adjacent nodes
                getAdjacentNodes(node).forEach { adj ->
                    if (distances[adj.first] > (distances[node] + adj.second)) {
                        distances[adj.first] = distances[node] + adj.second
                        queue.add(Pair(distances[adj.first], adj.first))
                    }
                }
            }
        }

        return distances[end]
    }

    //Returns a pair of Node, Value
    private fun getAdjacentNodes(node: Int): List<Pair<Int, Int>> {
        val coordinate = Pair(node % mapWidth, node / mapHeight)
        val edges = ArrayList<Pair<Int, Int>>()

        if (coordinate.x > 0) {
            edges.add(Pair(node - 1, input[coordinate.y][coordinate.x - 1].digitToInt()))
        }

        if (coordinate.x < mapWidth - 1) {
            edges.add(Pair(node + 1, input[coordinate.y][coordinate.x + 1].digitToInt()))
        }

        if (coordinate.y > 0) {
            edges.add(Pair(node - mapWidth, input[coordinate.y - 1][coordinate.x].digitToInt()))
        }

        if (coordinate.y < mapHeight - 1) {
            edges.add(Pair(node + mapWidth, input[coordinate.y + 1][coordinate.x].digitToInt()))
        }

        return edges
    }

    fun solve1(): Int {
        return djkstra()
    }


}

@ExperimentalStdlibApi
private fun test() {
    Day15(testInput).apply {
        assertEquals(40, solve1())
        //assertEquals(2188189693529, solve2(40))
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    test()
    Day15(realInput).apply {
        println(solve1()) //722 Too high
//        println(solve2(40))
    }
}
