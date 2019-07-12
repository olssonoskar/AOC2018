package day8

import java.io.File
import java.lang.IllegalArgumentException

const val HEADER_SIZE = 2
const val INDEX_OFFSET = -1

fun main() {
    val nodeData = parseNodeData()
    val node = buildChildNode(nodeData).first
    println("Total metadata sum is ${node.sumMetaDataTotal()}")
    println("The value of the root node is ${node.getValue()}")
}

private fun buildChildNode(data : List<Int>) : Pair<Node, List<Int>> {
    if(data.size < 2) throw IllegalArgumentException("A valid node must contain a header")
    val childrenSize = data[0]
    val metadataSize = data[1]
    val currentNode = Node()

    var unconsumedData = data.drop(HEADER_SIZE)
    return if(childrenSize == 0) {
        unconsumedData.take(metadataSize).map { currentNode.addMetaData(it) }
        unconsumedData = unconsumedData.drop(metadataSize)
        Pair(currentNode, unconsumedData)
    } else {
        for(i in 0 until childrenSize) {
            val result = buildChildNode(unconsumedData)
            unconsumedData = result.second
            currentNode.addChild(result.first)
        }
        unconsumedData.take(metadataSize).map { currentNode.addMetaData(it) }
        unconsumedData = unconsumedData.drop(metadataSize)
        Pair(currentNode, unconsumedData)
    }
}

fun parseNodeData() : List<Int> {
    var input = File("resources\\navigationNodes")
        .useLines { it.first() }
    return input.split(" ").map { it.toInt() }
}

class Node {
    private val childNodes = mutableListOf<Node>()
    private val metaData = mutableListOf<Int>()

    fun sumMetaDataTotal() : Int {
        var result = 0
        for(node in childNodes) {
            result += node.sumMetaDataTotal()
        }
        result += sumMetaData()
        return result
    }

    fun getValue() : Int {
        var result = 0
        return if(childNodes.size > 0){
            for(data in metaData) {
                if(data > 0 && data <= childNodes.size) {
                    result += childNodes[data + INDEX_OFFSET].getValue()
                }
            }
            result
        } else {
            sumMetaData()
        }
    }

    fun addMetaData(data : Int) {
        metaData.add(data)
    }

    fun addChild(child : Node) {
        childNodes.add(child)
    }

    private fun sumMetaData() =
        metaData.reduce(Int::plus)
}