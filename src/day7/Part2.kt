package day7

val MAX_WORKERS = 5
val alphabet : List<String> = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ")
val completed = mutableSetOf<String>()
var workers = listOf<Worker>()

fun main() {
    val taskTree = parseTasks()
    val duration = timeBuild(taskTree)
    println("It took $duration seconds to build the sleigh for $MAX_WORKERS workers")
}

fun timeBuild(taskTree: TaskTree) : Int {
    var elapsedTime = 0
    var availableTasks : MutableList<String> = taskTree.getInitialTasks().toMutableList()

    while (availableTasks.isNotEmpty() || workers.any { !it.isFinished() }) {
        availableTasks.sort()
        workers = setupWorkers(workers, availableTasks)
        while (workers.none { it.isFinished() }) {
            workers.map { it.doWork() }
            elapsedTime += 1
        }
        workers.filter { it.isFinished() }
            .map { completeTask(it.currentJob ,taskTree.getSubTasksOf(it.currentJob), availableTasks) }
    }
    return elapsedTime
}

fun completeTask(currentJob : String, subTasks : List<TaskTree.Task>, availableTasks : MutableList<String> ) {
    completed.add(currentJob)
    for (subTask in subTasks) {
        if(completed.containsAll(subTask.getParents()) && !availableTasks.contains(subTask.id)
            && !completed.contains(subTask.id)) {
            availableTasks.add(subTask.id)
        }
    }
}

fun setupWorkers(workers : List<Worker>, tasks : MutableList<String> ) : List<Worker> {
    var currentlyWorking = workers.filter { !it.isFinished() }
    val freeWorkers = MAX_WORKERS - currentlyWorking.size
    for( i in 0 until freeWorkers) {
        if(tasks.isNotEmpty()) {
            val nextTask = tasks.removeAt(0)
            currentlyWorking = currentlyWorking.plus(Worker(nextTask))
        }
    }
    return currentlyWorking
}

class Worker (
    val currentJob : String
) {
    private var timeWorked = 0

    fun isFinished () =
        timeWorked >= ( 60 + alphabet.indexOf(currentJob) + 1)

    fun doWork() {
        timeWorked++
    }
}