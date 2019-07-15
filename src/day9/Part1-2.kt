package day9

import java.util.ArrayDeque
import kotlin.math.abs

fun main() {
    val playerAmount = 418
    val marbleAmount = 71339 * 100
    val game = MarbleMania(playerAmount, marbleAmount)
    println("The winner got a score of ${game.play()}")
}

private class MarbleMania(players: Int, marbles: Int) {
    val players = LongArray(players)
    val marbleAmount = marbles
    val board = ArrayDeque<Int>().also { it.add(0) }

    fun play() : Long {
        (1..marbleAmount).forEach { marble ->
            if(marble % 23 == 0) {
                players[marble % players.size] += marble + with(board) {
                    shiftMarbles(-7)
                    removeFirst().toLong()
                }
                shiftMarbles(1)
            } else {
                shiftMarbles(1)
                board.addFirst(marble)
            }
        }
        return players.max() ?: 0
    }

    private fun shiftMarbles(positions : Int) {
        if (positions > 0) {
            repeat(positions) {
                board.addFirst(board.removeLast())
            }
        } else if (positions < 0){
            repeat(abs(positions)) {
                board.addLast(board.removeFirst())
            }
        }
    }
}