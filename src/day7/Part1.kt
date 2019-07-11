package day7

import java.io.File

fun main() {
    val taskTree = parseTasks()
    println(findOrder(taskTree))
}

fun findOrder(taskTree : TaskTree) : String {
    var order = ""
    val completed = mutableSetOf<String>()
    var availableTasks : MutableList<String> = taskTree.getInitialTasks().toMutableList()
    while (availableTasks.isNotEmpty()) {
        availableTasks.sort()
        val nextTask =  availableTasks.removeAt(0)
        order += nextTask
        completed.add(nextTask)
        for (subTask in taskTree.getSubTasksOf(nextTask)) {
            if(completed.containsAll(subTask.getParents()) && !availableTasks.contains(subTask.id)
                && !completed.contains(subTask.id)) {
                availableTasks.add(subTask.id)
            }
        }
    }
    return order
}

fun parseTasks() : TaskTree{
    val tree = TaskTree()
    File("resources\\sleighTasks")
        .forEachLine {
            val params = it.split(" ")
            val parent = params[1]
            val child = params[7]
            tree.insert(child, parent)
        }
    return tree
}