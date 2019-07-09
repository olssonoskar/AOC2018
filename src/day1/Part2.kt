package day1

import java.io.File

fun main() {
    val frequencies = initFrequencies()
    var seenFrequencies = mutableSetOf<Int>()
    var currentFreq = 0
    var counter = 0
    while (!seenFrequencies.contains(currentFreq)) {
        seenFrequencies.add(currentFreq)
        currentFreq += frequencies[counter++ % frequencies.size]
    }
    println(currentFreq)
}

fun initFrequencies(): List<Int> {
    var frequencies = ArrayList<Int>()
    File("resources\\frequencyInput")
        .forEachLine {
            val input = it.toInt()
            frequencies.add(input)
        }
    return frequencies
}