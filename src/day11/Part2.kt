package day11

/**
 * Solution inspired by tginsberg https://todd.ginsberg.com/post/advent-of-code/2018/day11/
 * since solution for part 1 was way to slow
 * Uses a https://en.wikipedia.org/wiki/Summed-area_table to limit square counting
 */

fun main() {
    val battery = Battery()
    val power = battery.findMaxAny()
    println("The best power square is ${power.power} with size ${power.size},  located at (${power.x},${power.y})")
}

class Battery {

    val serialNumber = 1723
    val cellRow = 300
    val summedAreaTable = createSummedAreaTable()

    fun findMaxAny() : PowerSquare {
        return findMaxFor(1, cellRow)
    }

    private fun findMaxFor(minSize : Int, maxSize : Int) : PowerSquare {
        var bestSquare = PowerSquare(0, 0, -1, -1)
        (minSize..maxSize).forEach { size ->
            (0 until cellRow - size).forEach { x ->
                (0 until cellRow - size).forEach { y ->
                    val squarePower = calcSquarePower(x, y, size)
                    if (squarePower > bestSquare.power) {
                        bestSquare = PowerSquare(squarePower, size, x + 1, y + 1)
                    }
                }
            }
        }
        return bestSquare
    }

    fun calcSquarePower(x : Int, y: Int, size: Int) : Int {
        val upperLeft = summedAreaTable[x][y]
        val upperRight = summedAreaTable[x + size][y]
        val lowerLeft = summedAreaTable[x][y + size]
        val lowerRight = summedAreaTable[x + size][y + size]
        return lowerRight + upperLeft - upperRight - lowerLeft
    }

    private fun createSummedAreaTable() : Array<IntArray> {
        val table = Array(cellRow) {IntArray(cellRow)}
        // Each cell is its own value plus the closest above and left, minus diagonal above
        (0 until cellRow).forEach{ x ->
            (0 until cellRow).forEach { y ->
                val currentCell = calculateCell(x, y)
                val above = if (y == 0) 0 else table[x][y - 1]
                val left = if (x == 0) 0 else table[x - 1][y]
                val upperLeft =  if (x == 0 || y == 0) 0 else table[x - 1][y - 1]
                table[x][y] = currentCell + above + left - upperLeft
            }
        }
        return table
    }

    private fun calculateCell(x: Int, y: Int) : Int {
        val rackId = x + 10
        return ((rackId * y + serialNumber) * rackId).hundreds() - 5
    }

    private fun Int.hundreds(): Int = (this / 100) % 10
}

class PowerSquare(val power: Int, val size: Int, val x: Int, val y:Int)