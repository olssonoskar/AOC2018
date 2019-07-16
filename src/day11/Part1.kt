package day11

// Working solution for part 1 but way to slow for part 2

fun main() {
    val battery = BatterySlow()
    val position = battery.findMaxPower()
    println("The best power square (3x3) is located at (${position.first},${position.second})")
}

class BatterySlow {
    private val serialNumber = 18
    private val batteryCells = 300
    private val cells = Array(batteryCells) {IntArray(batteryCells, init = { Int.MAX_VALUE } )}

    fun findMaxPower() : Pair<Int, Int> {
        var maxPower = 0
        var maxPowerPosition = Pair(-1, -1)
        for(x in 0 until (batteryCells - 2)) {
            for(y in 0 until (batteryCells - 2)) {
                val power = sumFuelSquare(x, y)
                if(power > maxPower) {
                    maxPower = power
                    maxPowerPosition = Pair(x, y)
                }
            }
        }
        println("Max power found: $maxPower")
        return maxPowerPosition
    }

    private fun sumFuelSquare(x: Int, y: Int, size: Int = 3) : Int {
        var totalSum = 0
        for(i in x until (x + size)) {
            for(j in y until (y + size)) {
                if(cells[i][j] == Int.MAX_VALUE) {
                    cells[i][j] = calculateCell(i , j)
                }
                totalSum += cells[i][j]
            }
        }
        return totalSum
    }

    private fun calculateCell(x: Int, y: Int) : Int {
        val rackId = x + 10
        return ((rackId * y + serialNumber) * rackId).hundreds() - 5
    }

    private fun Int.hundreds(): Int = (this / 100) % 10
}