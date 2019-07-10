package day4

import java.io.File
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Part 1 - Duration & Part 2 - Frequency
private val metric = Metric.FREQUENCY

fun main() {
    val events = parseEvents()
    val guard = findSleepyGuard(events, metric)
    if(metric == Metric.DURATION) {
        println("The guard sleeping the most is ${guard.id} who slept for a total of ${guard.getDuration()}" +
                " with most times slept at ${guard.getMostTimesSleptAt()}")
    } else {
        println("The guard sleeping the most at one single point is ${guard.id} who slept for a total of ${guard.getMostTimesSlept()} times" +
                " at minute ${guard.getMostTimesSleptAt()}")
    }
}

private fun parseEvents() : List<EventInfo> {
    val events = mutableListOf<EventInfo>()
    File("resources\\GuardEvents")
        .forEachLine {
            val time = parseTime(it.substring(1, 17))
            val params = it.substring(19).split(" ")
            events.add(toEvent(params, time))
        }
    return events.sortedBy { it.time }
}

private fun toEvent(params : List<String>, time : LocalDateTime) : EventInfo {
    return when (params[1].toLowerCase()) {
        "up" -> EventInfo(Event.AWAKE, time)
        "asleep" -> EventInfo(Event.ASLEEP, time)
        else -> {
            EventInfo(Event.SHIFT, time, params[1].substring(1).toInt())
        }
    }
}

private fun parseTime(dateString : String) : LocalDateTime =
    LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

private fun findSleepyGuard(events : List<EventInfo>, baseOn : Metric) : SleepyGuard {
    val guards = mutableMapOf<Int, SleepyGuard>()
    var currentGuard = SleepyGuard(id = -1)
    var previousEntry = EventInfo(Event.AWAKE, LocalDateTime.now())
    for(entry in events) {
        when (entry.event) {
            Event.SHIFT -> {
                if(currentGuard.id != -1) {
                    guards[currentGuard.id] = currentGuard
                }
                currentGuard = guards.get(entry.id) ?: SleepyGuard(id = entry.id)
            }
            Event.ASLEEP -> {
                previousEntry = entry
            }
            Event.AWAKE -> {
                if(previousEntry.event == Event.ASLEEP) {
                    currentGuard.addToDuration(getTimeSlept(previousEntry.time, entry.time))
                    for( i in previousEntry.time.minute until entry.time.minute) {
                        currentGuard.increaseAsleepAt(i)
                    }
                }
            }
        }
    }
    return if(baseOn == Metric.DURATION) {
        guards.maxBy { it.value.getDuration() }?.value ?: SleepyGuard(id = -1)
    } else {
        guards.maxBy { it.value.getMostTimesSlept() }?.value ?: SleepyGuard(id = -1)
    }

}

private fun getTimeSlept(asleep : LocalDateTime, awake: LocalDateTime) : Int {
    val timeSlept = Duration.between(asleep, awake).toMinutes()
    if(timeSlept > 60) {
        throw IllegalArgumentException("Time slept shouldn't be more than 1 hour: $asleep, $awake")
    }
    return timeSlept.toInt()
}

private class SleepyGuard (
    private var duration: Int = 0,
    private val sleepingDuringMidnight: IntArray = IntArray(size = 60),
    private var mostTimesSleptAt: Int = 0,
    private var mostTimesSlept: Int = 0,
    val id: Int
) {
    fun getDuration() : Int {
        return duration
    }

    fun getMostTimesSleptAt() : Int {
        return mostTimesSleptAt
    }

    fun getMostTimesSlept() : Int {
        return mostTimesSlept
    }

    fun increaseAsleepAt(minute : Int) {
        val newAmount = sleepingDuringMidnight[minute] + 1
        sleepingDuringMidnight[minute] = newAmount
        if ( newAmount > mostTimesSlept ) {
            mostTimesSlept = newAmount
            mostTimesSleptAt = minute
        }
    }

    fun addToDuration(duration: Int) {
        this.duration += duration
    }
}

private data class EventInfo (
    val event : Event,
    val time: LocalDateTime,
    val id : Int = -1
)

private enum class Event {
    ASLEEP,
    AWAKE,
    SHIFT
}

private enum class Metric {
    DURATION,
    FREQUENCY
}