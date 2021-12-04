import java.io.File

class Day4 {

    val input = File("inputs/Day4Input.txt").readLines()

    class BingoCard(val matrixLines: List<MutableList<Int>>) {

        private val size = matrixLines.size

        private var didBingo = false

        fun markNumber(number: Int) {
            for (column in 0 until size) {
                for (line in 0 until size) {
                    if (matrixLines[line][column] == number) {
                        matrixLines[line][column] = -1
                        return
                    }
                }
            }
        }

        private fun checkLines(): Boolean {
            matrixLines.forEach {
                if (it.all { it == -1 }) {
                    didBingo = true
                    return true
                }
            }
            return false
        }

        private fun checkColumns(): Boolean {
            for (column in 0 until size) {
                for (line in 0 until size) {
                    if (matrixLines[line][column] != -1) break
                    if (line == size - 1) {
                        didBingo = true
                        return true
                    }
                }
            }
            return false
        }

        fun checkBingo(): Boolean = didBingo || checkLines() || checkColumns()

        fun getRemainingSum(): Int = matrixLines.sumBy {
            it.sumBy { if (it != -1) it else 0 }
        }

    }

    private fun parseCards(): List<BingoCard> {
        val parsed = input.drop(2).windowed(5, 6).map { items ->
            items.map {
                it.trim()
                    .replace("  ", " ")
                    .split(" ")
                    .map { it.toInt() }
                    .toMutableList()
            }
        }

        return parsed.map { BingoCard(it) }
    }

    fun solve1(): Int {
        val numbers = input[0].split(",").map { it.toInt() }

        val cards = parseCards()

        numbers.forEach { number ->
            cards.forEach { card ->
                card.markNumber(number)
                if (card.checkBingo()) {
                    println("Bingo -> ${card.matrixLines}")
                    return card.getRemainingSum() * number
                }
            }
        }

        return -1
    }

    fun solve2(): Int {
        val numbers = input[0].split(",").map { it.toInt() }

        val cards = parseCards()

        var lastBingo: BingoCard? = null
        var winNumber = 0

        numbers.forEach { number ->
            cards.forEach { card ->
                if(!card.checkBingo()) {
                    card.markNumber(number)
                    if (card.checkBingo()) {
                        lastBingo = card
                        winNumber = number
                    }
                }
            }
        }

        return (lastBingo ?: return -1).getRemainingSum() * winNumber

    }

}

fun main() {
    Day4().apply {
        println(solve1())
        println(solve2())
    }
}
