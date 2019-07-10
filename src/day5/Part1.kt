package day5

import java.io.File

fun main() {
    val polymer = File("resources\\polymer").useLines { it.first() }
    val reducedPolymer = reducePolymer(polymer)
    println("The reduced poly is $reducedPolymer with length ${reducedPolymer.length}")
}

fun reducePolymer(polymer: String) : String{
    var isReduced = false
    val reduceIndex = mutableListOf<Int>()
    for (i in 0 until polymer.length) {
        if(isReduced) {
            isReduced = false
            continue
        }
        if(i+1 < polymer.length && isReducible(polymer[i], polymer[i+1])) {
            isReduced = true
            reduceIndex.add(i)
        }
    }
    return if(reduceIndex.size > 0) {
        val reducedPoly = reduce(polymer, reduceIndex)
        reducePolymer(reducedPoly)
    } else {
        polymer
    }
}

private fun isReducible (unit : Char, otherUnit : Char) : Boolean {
    return if(unit.isLowerCase()) {
        unit.toUpperCase() == otherUnit
    } else {
        unit.toLowerCase() == otherUnit
    }
}

private fun reduce(polymer : String, indexes : List<Int>) : String {
    var reducedPoly : String = polymer
    var unitsReduced = 0
    for(index in indexes) {
        val indexOffset = index - (unitsReduced*2)
        reducedPoly = reducedPoly.removeRange(indexOffset, indexOffset+2)
        unitsReduced += 1
    }
    return reducedPoly
}