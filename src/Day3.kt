import java.io.File

@ExperimentalUnsignedTypes
class Day3 {

    val input = File("inputs/Day3Input.txt").useLines { it.toList() }

    fun solve1(): Int {
        val length = input.first().length
        var epsilon = 0u
        for (i in length - 1 downTo 0) {
            val oneCount = input.count { it[i] == '1' }
            if(oneCount > input.size/2) {
                epsilon = epsilon or (1u shl i)
            }
        }

        val gamma = epsilon xor 0xFFFu

        println("${epsilon.toString(2).padStart(length, '0')} ${gamma.toString(2).padStart(length, '0')}")
        return (gamma * epsilon).toInt()
    }

    fun solve2(): Int {

        var filteredSet = HashSet(input)

        var position = 0
        while(filteredSet.size > 1) {
            val grouped = filteredSet.groupBy { it[position] }
            val toRemove = if((grouped['0']?.size ?: 0) > (grouped['1']?.size ?: 0)) {
                '1'
            } else {
                '0'
            }
            filteredSet.removeIf { it[position] == toRemove }
            position++
        }

        val value1 = filteredSet.first().toInt(2)

        filteredSet = HashSet(input)

        position = 0
        while(filteredSet.size > 1) {
            val grouped = filteredSet.groupBy { it[position] }
            val toRemove = if((grouped['0']?.size ?: 0) <= (grouped['1']?.size ?: 0)) {
                '1'
            } else {
                '0'
            }
            filteredSet.removeIf { it[position] == toRemove }
            position++
        }

        val value2 = filteredSet.first().toInt(2)

        return value1 * value2
    }

    /*
    101111111101 010000000010
    3148794
    2795310
     */
}

@ExperimentalUnsignedTypes
fun main() {
    Day3().apply {
        println(solve1())
        println(solve2())
    }
}
