package day1

import java.io.File

fun main() {

    var currentFreq = 0
    File("C:\\Users\\Oskar\\Documents\\GitHub\\AOC2018\\src\\day1\\frequencyInput")
        .forEachLine {
            val input = it.toInt()
            currentFreq += input
        }
    println(currentFreq)
}

