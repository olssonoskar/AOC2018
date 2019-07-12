package day9

import java.util.ArrayDeque

fun main() {
    val playerAmount = 9
    val marbleAmount = 25

    val players = IntArray(size = playerAmount)
    val marbles = ArrayDeque<Int>().also { it.add(0) }

    (1..marbleAmount).forEach { marble ->
        if(marble % 23 == 0) {

        }
    }


}