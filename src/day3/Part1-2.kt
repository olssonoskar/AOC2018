package day3

import java.io.File

fun main() {
    var fabricSquare = FabricSquare()
    val claims = getClaims()
    for (claim in claims) {
        fabricSquare.setClaim(claim)
    }
    println("Amount of spaces that are in conflict :" + fabricSquare.getConflicted())
    for (claim in claims) {
        if(fabricSquare.isClaimUncontested(claim)) {
            println("Claim is valid: " + claim.id)
            break
        }
    }
}

fun getClaims() : List<Claim> {
    var claims = mutableListOf<Claim>()
    File("resources\\claims")
        .forEachLine {
            val params = it.split(" ")
            val id = params[0].removeRange(0,1)
            val colonIndex = params[2].length
            val (x, y) = params[2].substring(0, colonIndex-1).split(",")
            val (rangeX, rangeY) = params[3].split("x")
            claims.add(Claim(id.toInt(), x.toInt(), y.toInt(), rangeX.toInt(), rangeY.toInt()))
        }
    return claims
}

data class Claim(
    val id : Int, val x : Int,
    val y : Int,
    val rangeX : Int, val rangeY : Int)

class FabricSquare {
    private var fabric: Array<IntArray> = Array(1000) {IntArray(1000)}
    private var conflicted = 0

    fun getClaim(x : Int, y : Int) : Int {
        return fabric[x][y]
    }

    fun getConflicted() : Int {
        return conflicted
    }

    private fun setClaim(x : Int, y : Int, value : Int) {
        when (getClaim(x, y)) {
            0 ->  fabric[x][y] = value
            -1 -> {}
            else -> {
                fabric[x][y] = -1
                conflicted++
            }
        }
        return
    }

    fun setClaim(claim : Claim) {
        for(i in 0 until claim.rangeX) {
            for(j in 0 until claim.rangeY) {
                setClaim(claim.x + i, claim.y + j, claim.id)
            }
        }
    }

    fun isClaimUncontested(claim : Claim) : Boolean {
        for(i in 0 until claim.rangeX) {
            for(j in 0 until claim.rangeY) {
                if(getClaim(claim.x + i, claim.y + j) != claim.id) {
                    return false
                }
            }
        }
        return true
    }
}