package day6

import java.io.File
import kotlin.math.abs

fun main() {
    val coordinates = parseCoordinates()
    val coordinate = findLargestArea(coordinates)
    println("The largest finite coordinate has a size of ${coordinate.area}")
    val area = findCompactArea(coordinates)
    println("The region in proximity to all coordinates has a size of $area")
}

private fun parseCoordinates(): List<Coordinate> {
    val coords = mutableListOf<Coordinate>()
    File("resources\\ChronalCoordinates")
        .forEachLine {
            val params = it.split(",")
            coords.add(Coordinate(params[0].toInt(), params[1].trim().toInt()))
        }
    return coords
}

// Part 1 - find largest finite region for a coordinate
private fun findLargestArea(coordinates: List<Coordinate>) : Coordinate {
    val width = coordinates.maxBy { it.x }?.x ?: 0
    val height = coordinates.maxBy { it.y }?.y ?: 0
    for (x in 0 until width) {
        for (y in 0 until height) {
            val coord = findClosest(x, y, coordinates)
            if (coord != null) {
                coord.area += 1
                if(coord.finite) {
                    coord.finite = isFinite(x, y, width, height)
                }
            }
        }
    }
    return coordinates
        .filter { it.finite }
        .maxBy { it.area } ?: Coordinate(-1, -1 , -1)
}

fun isFinite (x: Int, y: Int, width: Int, height: Int) =  (x != 0 && x != width-1 && y != 0 && y != height-1)

private fun findClosest(x: Int, y: Int, coords : List<Coordinate>) : Coordinate? {
    var closestCoord : Coordinate? = null
    var shortestDistance = Integer.MAX_VALUE
    for(coordinate in coords) {
        val distanceX = abs(coordinate.x - x)
        val distanceY = abs(coordinate.y - y)
        val currentDistance = (distanceX + distanceY)
        if ( currentDistance < shortestDistance) {
            shortestDistance = currentDistance
            closestCoord = coordinate
        } else if ( shortestDistance == currentDistance) {
            closestCoord = null
        }
    }
    return closestCoord
}

// Part 2 - Measure region within proximity to all other Coords
private fun findCompactArea(coordinates: List<Coordinate>) : Int {
    val width = coordinates.maxBy { it.x }?.x ?: 0
    val height = coordinates.maxBy { it.y }?.y ?: 0
    var areaCount = 0
    for (x in 0 until width) {
        for (y in 0 until height) {
            if (whitinProximityAll(x, y, coordinates)) areaCount += 1
        }
    }
    return areaCount
}

private fun whitinProximityAll(x: Int, y: Int, coords: List<Coordinate>) : Boolean {
    var totalDistance = 0
    for(coordinate in coords) {
        val distanceX = abs(coordinate.x - x)
        val distanceY = abs(coordinate.y - y)
        totalDistance += distanceX + distanceY
    }
    return totalDistance < 10_000
}

private data class Coordinate(
    val x: Int,
    val y: Int,
    var area: Int = 0,
    var finite : Boolean = true
)