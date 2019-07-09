package day2

import java.io.File


fun main() {
    var doubleLetterIds = 0
    var tripleLetterIds = 0
    val boxIds = getBoxIds()

    for(id in boxIds) {
        val occurances = findOccurancesInId(id)
        doubleLetterIds += occurances.first
        tripleLetterIds += occurances.second
    }
    println(doubleLetterIds * tripleLetterIds)
}

private fun findOccurancesInId (id : String) : Pair<Int, Int> {
    var letterCount = mutableMapOf<Char, Int>()
    for(letter in id) {
        letterCount.merge(letter, 1, Int::plus)
    }
    return checkMatches(letterCount)
}

private fun checkMatches(map : Map<Char, Int>) : Pair<Int, Int> {
    var foundDoubleLetter = 0
    var foundTripleLetter = 0
    for(entry in map) {
        if(entry.value == 2) {
            foundDoubleLetter = 1
        }
        else if(entry.value == 3) {
            foundTripleLetter = 1
        }
        if(foundDoubleLetter == 1 && foundTripleLetter == 1) {
            break
        }
    }
    return Pair(foundDoubleLetter, foundTripleLetter)
}

fun getBoxIds() : List<String> {
    var boxIds = ArrayList<String>()
    File("resources\\boxIds")
        .forEachLine { boxIds.add(it) }
    return boxIds
}