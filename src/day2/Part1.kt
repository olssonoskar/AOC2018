package day2

import java.io.File


fun main() {
    var doubleLetterIds = 0
    var tripleLetterIds = 0
    val boxIds = initBoxIds()

    for(id in boxIds) {
        val occurances = findOccurancesInId(id)
        doubleLetterIds += occurances.first
        tripleLetterIds += occurances.second
    }
    println(doubleLetterIds * tripleLetterIds)
}

fun findOccurancesInId (id: String): Pair<Int, Int> {
    var letterCount = mutableMapOf<Char, Int>()
    for(letter in id) {
        letterCount.merge(letter, 1, Int::plus)
    }
    return checkMatches(letterCount)
}

fun checkMatches(map: Map<Char, Int>): Pair<Int, Int> {
    var foundDouble = 0
    var foundTriple = 0
    for(entry in map) {
        if(entry.value == 2) {
            foundDouble = 1
        }
        if(entry.value == 3) {
            foundTriple = 1
        }
        if(foundDouble == 1 && foundTriple == 1) {
            break
        }
    }
    return Pair(foundDouble, foundTriple)
}

fun initBoxIds(): List<String> {
    var boxIds = ArrayList<String>()
    File("C:\\Users\\Oskar\\Documents\\GitHub\\AOC2018\\src\\day2\\boxIds")
        .forEachLine {
            boxIds.add(it)
        }
    return boxIds
}