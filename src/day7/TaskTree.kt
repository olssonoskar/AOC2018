package day7

import java.lang.IllegalArgumentException

class TaskTree {

    private val root = Task("root")

    fun insert(task: String, dependsOnTask: String) {
        val parent = getTask(dependsOnTask)
        val child = existsOrNew(task)
        child.addParent(dependsOnTask)
        if (parent == null) {
            val rootTask = Task(dependsOnTask)
            rootTask.addSubTask(child)
            root.addSubTask(rootTask)
        } else {
            parent.addSubTask(child)
            root.removeSubTask(child)
        }
    }

    private fun existsOrNew(id : String) : Task {
        return getTask(id) ?: Task(id)
    }

    private fun getTask(id: String): Task? {
        return getTask(id, root)
    }

    private fun getTask(id: String, task: Task): Task? {
        var requestedTask = task.getSubTasks().firstOrNull{ task -> task.id == id }
        if (requestedTask != null) {
            return requestedTask
        }
        for (subTask in task.getSubTasks()) {
            requestedTask = getTask(id, subTask)
            if (requestedTask != null) {
                return  requestedTask
            }
        }
        return null
    }

    fun getInitialTasks() : List<String> {
        return root.getSubTasks().map { task -> task.id }
    }

    fun getSubTasksOf(task: String) : List<Task> {
        return getTask(task)?.getSubTasks() ?:
        throw IllegalArgumentException("No node with id $task")
    }

    data class Task (
        val id : String)
    {
        private val subTasks = mutableListOf<Task>()
        private val requiredTasks = mutableListOf<String>()

        fun getSubTasks() : List<Task> {
            return subTasks
        }

        fun getParents() : List<String> {
            return requiredTasks
        }

        internal fun addParent(id : String) {
            if(requiredTasks.contains(id)) { return }
            requiredTasks.add(id)
        }

        internal fun addSubTask(subTask : Task) {
            if(subTasks.contains(subTask)) { return }
            subTasks.add(subTask)
        }

        internal fun removeSubTask(subTask: Task) {
            if(subTasks.contains(subTask)) {
                subTasks.remove(subTask)
            }
        }
    }
}

