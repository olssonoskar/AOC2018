package day5

import java.io.File

fun main() {
    val polymer = File("resources\\polymer").useLines { it.first() }
    val reduction = reduceWithElimination(polymer)
    println("Best reduction achieved by deleting ${reduction.first} resulting in length ${reduction.second}")
}

fun reduceWithElimination(polymer : String) : Pair<Char, Int> {
    val testedUnits = mutableSetOf<Char>()
    var bestReduction : Pair<Char, Int> = Pair(' ', Int.MAX_VALUE)
    for(unit in polymer) {
        if(!testedUnits.contains(unit.toLowerCase())) {
            testedUnits.add(unit.toLowerCase())
            val polyAfterElimination = eliminateUnit(polymer, unit)
            val currentLength = reducePolymer(polyAfterElimination).length
            if ( currentLength < bestReduction.second) {
                bestReduction = Pair(unit, currentLength)
            }
        }
    }
    return bestReduction
}

fun eliminateUnit(poly : String, unit : Char) : String = poly.filter { !it.equals(unit, true) }