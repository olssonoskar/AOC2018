package day2

fun main() {
    val commonId = findCommonId(getBoxIds())
    println("CommonId: $commonId")
}

//CommonId is the equal part of two strings which differ by exactly one character
private fun findCommonId(allIds : List<String>) : String {
    for(id in allIds) {
        for (otherId in allIds) {
            if (id === otherId) continue
            val singleDiffIndex = findSingleDiffIndex(id, otherId)
            if (singleDiffIndex != -1) {
                println("Diffs by one: $id - $otherId at pos $singleDiffIndex")
                return id.removeRange(singleDiffIndex, singleDiffIndex + 1)
            }
        }
    }
    return ""
}

private fun findSingleDiffIndex(id : String, otherId : String) : Int {
    var diffIndex = -1
    for(i in 0 until id.length) {
        if(id[i] != otherId[i]) {
            if( diffIndex == -1 ) {
                diffIndex = i
            } else {
                diffIndex = -1
                break
            }
        }
    }
    return diffIndex
}
