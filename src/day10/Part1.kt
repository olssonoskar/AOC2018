package day10

import java.io.File

fun main() {
    val lights = mutableSetOf<Light>()
    File("resources\\lights").forEachLine { lights.add(Light.of(it)) }
    val nightSky = NightSky(lights)
    println("Sky aligns at ${nightSky.align()}")
}

private class NightSky(val lights: MutableSet<Light>) {
    var currentTime = 0

    fun align() : Int {
        var aligned = false
        while (!aligned) {
            var hasNeighbor = false
            for (light in lights) {
                hasNeighbor = lights.filter { otherLight -> light !== otherLight  }
                    .filter { otherLight -> light.neighborOf(otherLight) }
                    .count() > 0
                if(!hasNeighbor) {
                    currentTime++
                    lights.map { it.step() }
                    break
                }
            }
            if ( hasNeighbor ) aligned = true
        }
        printSky()
        return currentTime
    }

    fun printSky() {
        val minX = lights.minBy { it.x }?.x ?: 0
        val minY = lights.minBy { it.y }?.y ?: 0
        val maxX = lights.maxBy { it.x }?.x ?: 0
        val maxY = lights.maxBy { it.y }?.y ?: 0
        for (y in minY until maxY + 1) {
            var currentLine = ""
            val currentLights = lights.filter{ it.y == y }
            for(x in minX until maxX + 1) {
                currentLine += if(currentLights.filter { it.x == x }.count() > 0) {
                    " #"
                } else {
                    " ."
                }
            }
            println(currentLine)
        }
    }
}

private data class Light(var x : Int, var y : Int, val dX : Int, val dY : Int) {

    private fun neighborY (x: Int, otherX : Int) = x == otherX + 1 || x == otherX - 1
    private fun neighborX (x: Int, otherX : Int) = x == otherX + 1 || x == otherX - 1
    fun neighborOf (other: Light) : Boolean {
        return (neighborX(x, other.x) && y == other.y) ||
                (neighborY(y, other.y) && x == other.x) ||
                (neighborX(x, other.x) && neighborY(y, other.y))
    }

    fun step() {
        x += dX
        y += dY
    }

    companion object {
        fun of(input: String): Light =
            input.split(",", "<", ">").map { it.trim() }.run {
                Light(this[1].toInt(), this[2].toInt(), this[4].toInt(), this[5].toInt())
            }
    }
}
