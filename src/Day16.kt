import java.io.File
import java.lang.StringBuilder
import kotlin.test.assertEquals

private val realInput = File("inputs/Day16Input.txt").readLines().first()
private val testInput = File("inputs/Day16TestInput.txt").readLines().first()

class Day16(val input: String) {

    val binary = input.map {
        it.toString().toInt(16).toString(2).padStart(4, '0')
    }.joinToString("")

    sealed class Packet(val version: String, val type: Int) {

        abstract fun getVersionSum(): Int

        abstract fun getValue(): Long

        class NumberPacket(version: String, type: Int, val number: String) : Packet(version, type) {
            override fun getVersionSum(): Int {
                return version.toInt(2)
            }

            override fun getValue(): Long = number.toLong(2)
        }

        class OperatorPacket(
            version: String,
            type: Int,
            val lengthType: String,
            val subPackets: List<Packet>
        ) : Packet(version, type) {
            override fun getVersionSum(): Int {
                return subPackets.sumBy { it.getVersionSum() } + version.toInt(2)
            }

            override fun getValue(): Long = when(type) {
                0 -> subPackets.sumByDouble { it.getValue().toDouble() }.toLong() //Sum
                1 -> subPackets.fold(1) { acc, packet -> acc * packet.getValue() } //Multiply
                2 -> subPackets.minOf { it.getValue() }
                3 -> subPackets.maxOf { it.getValue() }
                5 -> if(subPackets.first().getValue() > subPackets.last().getValue()) 1 else 0 //gt
                6 -> if(subPackets.first().getValue() < subPackets.last().getValue()) 1 else 0 //lt
                7 -> if(subPackets.first().getValue() == subPackets.last().getValue()) 1 else 0 //eq
                else -> throw IllegalArgumentException("Invalid packet!")
            }
        }
    }

    //Returns a pair with the Packet and the size of the packet
    fun readPacket(offset: Int): Pair<Int, Packet>? = try {

        var i = offset

        val version = binary.substring(i, i + 3)
        i += 3
        val type = binary.substring(i, i + 3).toInt(2)
        i += 3

        if (type == 4) {
            //Literal value
            val number = StringBuilder()
            while (binary[i] != '0') {
                number.append(binary.substring(i + 1, i + 5))
                i += 5
            }
            number.append(binary.substring(i + 1, i + 5))
            i += 5

            Pair(i, Packet.NumberPacket(version, type, number.toString()))

        } else {

            //Operator

            val lengthTypeId = binary[i++]

            val subPackets = ArrayList<Packet>()

            when (lengthTypeId) {
                '0' -> {
                    val totalLength = binary.substring(i, i + 15).toInt(2)
                    i += 15
                    val initialOffset = i
                    while (i - initialOffset < totalLength) {
                        readPacket(i)?.let {
                            i = it.first
                            subPackets.add(it.second)
                        }
                    }
                }
                '1' -> {
                    val qtyPackets = binary.substring(i, i + 11).toInt(2)
                    i += 11
                    repeat(qtyPackets) {
                        readPacket(i)?.let {
                            i = it.first
                            subPackets.add(it.second)
                        }
                    }
                }
            }

            Pair(i, Packet.OperatorPacket(version, type, lengthTypeId.toString(), subPackets))

        }

    } catch (e: StringIndexOutOfBoundsException) {
        null
    }


    fun solve1(): Int {

        var i = 0
        val packets = ArrayList<Packet>()
        var packet: Pair<Int, Packet>? = null
        do {
            packet = readPacket(i)
            packet?.let {
                i = it.first
                packets.add(it.second)
            }
        } while(packet != null)

        return packets.sumBy { it.getVersionSum() }
    }

    fun solve2(): Long {

        var i = 0
        val packets = ArrayList<Packet>()
        var packet: Pair<Int, Packet>? = null
        do {
            packet = readPacket(i)
            packet?.let {
                i = it.first
                packets.add(it.second)
            }
        } while(packet != null)

        return packets.first().getValue()

    }

}

private fun test() {
    assertEquals(16, Day16("8A004A801A8002F478").solve1())
    assertEquals(23, Day16("C0015000016115A2E0802F182340").solve1())
    assertEquals(31, Day16("A0016C880162017C3686B18A3D4780").solve1())

    assertEquals(3, Day16("C200B40A82").solve2())
    assertEquals(54, Day16("04005AC33890").solve2())
    assertEquals(7, Day16("880086C3E88112").solve2())
    assertEquals(9, Day16("CE00C43D881120").solve2())
    assertEquals(1, Day16("D8005AC2A8F0").solve2())
    assertEquals(0, Day16("F600BC2D8F").solve2())
}

fun main() {
    test()
    Day16(realInput).apply {
        println(solve1())
        println(solve2())
    }
}
