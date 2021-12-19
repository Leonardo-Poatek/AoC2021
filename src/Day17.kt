import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.test.assertEquals
import kotlin.test.assertTrue

//private val realInput = File("inputs/Day17Input.txt").readLines().first()

class Day17 {

    fun solve1(xRange: IntRange, yRange: IntRange): Pair<Int, Int> {

        /*
        1) Achar qual o min de X que precisamos pesquisar, pq eventualmente ele cai reto.
        O minimo é essa distancia que atinge o mais proximo do inicio. Calcula usando uma PA
        O maximo é a distancia do fim do alvo (Ex 100..200, seria 200);

        VMin = Primeiro cujo somatorio seja > Inicio

        Acha o Y min:
        Minimo de impulso pra cima pra que em VMin steps se chegue no lugar
        Depois de um tempo X, garantidamente a velocidade Y vai ser maior que o tamanho da area. nesse caso nao precisa testar mais.

        2)

         */

        val xMax = xRange.last

        val xMin = (0..xMax).first { (it * (it + 1)) / 2 > xRange.first }

        val xVelCandidates = (xMin..xMax)

        //This is dumb, I should find the right interval somehow
        var yMin = -1000
        var yMax = 1000

        var highestY = yMin
        var correctVelocities = HashSet<Pair<Int, Int>>()

        for (x in xVelCandidates) {

            //Theres gotta be a better max value to put here
            // The max Y value depends on the X.

            for (y in yMin..yMax) {

                var vX = x
                var vY = y

                var posX = 0
                var posY = 0
                var valid = true
                var currentMax = 0

                while (valid) {
                    posX += max(vX--, 0)
                    posY += vY--
                    currentMax = max(currentMax, posY)
                    if (posX in xRange && posY in yRange) {
                        highestY = maxOf(currentMax, highestY)
                        correctVelocities.add(Pair(x, y))
                        break
                    }
                    valid = posX <= xRange.last && posY >= yRange.first //X < direita e Y > fundo
                }
            }

        }
        return Pair(highestY, correctVelocities.size)
    }
}

private fun test() {
    assertEquals(45, Day17().solve1(20..30, -10..-5).first)
    assertEquals(112, Day17().solve1(20..30, -10..-5).second)
}

fun main() {
    test()
    Day17().apply {
        println(solve1(135..155, -102..-78))
    }
}
