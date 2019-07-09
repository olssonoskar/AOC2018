package day1

import java.io.File

fun main() {

    var currentFreq = 0
    File("resources\\frequencyInput")
        .forEachLine {
            val input = it.toInt()
            currentFreq += input
        }
    println(currentFreq)
}

