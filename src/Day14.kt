import java.io.File
import java.lang.StringBuilder
import kotlin.test.assertEquals

private val realInput = File("inputs/Day14Input.txt").readLines()
private val testInput = File("inputs/Day14TestInput.txt").readLines()

class Day14(val input: List<String>) {

    val template = input.first()//: LinkedList<Char> = LinkedList(input.first().toList())
    val insertions: Map<String, Char> = input.drop(2).map {
        it.split(" -> ").let { it.first() to it.last().first() }
    }.toMap()

    fun solve1(iterations: Int): Int {
        val template = StringBuilder(template)

        repeat(iterations) {
            var i = 0
            while (i < template.length - 1) {
                insertions["${template[i]}${template[i + 1]}"]?.let {
                    template.insert(i + 1, it)
                    i++
                }
                i++
            }
        }

        //println(template.toString())
        return template.groupingBy { it }.eachCount().let {
            it.maxByOrNull { it.value }!!.value - it.minByOrNull { it.value }!!.value
        }
    }

    data class MutableInt(var count: Long = 0L)

    fun solve2(iterations: Int): Long {

        val pairs = template.windowed(2).groupBy { it }.map { it.key to MutableInt(it.value.size.toLong()) }.toMap(mutableMapOf())

        val letterCount = template.groupBy { it }.map { it.key to MutableInt(it.value.size.toLong()) }.toMap(mutableMapOf())

        repeat(iterations) {

            val newPairs = HashMap<String, MutableInt>()

            pairs.forEach { pair ->
                if (pair.value.count > 0) {
                    insertions[pair.key]?.let {
                        val oldQnt = pair.value.count
                        pair.value.count = 0 //--
                        val pair1 = pair.key.first().toString() + it
                        val pair2 = it + pair.key.last().toString()
                        letterCount.getOrPut(it) { MutableInt() }.count += oldQnt //Nao eh ++ aqui, tem que multiplicar pelo numero que tem.
                        newPairs.getOrPut(pair1) { MutableInt() }.count += oldQnt
                        newPairs.getOrPut(pair2) { MutableInt() }.count += oldQnt
                    }
                }
            }

            newPairs.forEach {
                pairs.getOrPut(it.key){ MutableInt() }.count += it.value.count
            }
        }

        //println(template.toString())
        return letterCount.let {
            it.maxByOrNull { it.value.count }!!.value.count - it.minByOrNull { it.value.count }!!.value.count
        }
    }

}

private fun test() {
    Day14(testInput).apply {
        assertEquals(1588, solve1(10))
        assertEquals(2188189693529, solve2(40))
    }
}

fun main() {
    test()
    Day14(realInput).apply {
        println(solve1(10))
        println(solve2(40))
    }
}
