package dev.akif.dependencyfinder.logging

import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestIO: IO {
    private val logs: MutableList<String> = mutableListOf()
    private val inputs: Queue<String> = ArrayDeque()

    override fun print(message: String) {
        logs.add(message)
    }

    override fun die(message: String, error: Throwable?) {
        logs.add(message)
        error?.message?.let { logs.add(it) }
    }

    fun input(text: String) {
        inputs.offer(text)
    }

    override fun prompt(prefix: String): String =
        inputs.poll()

    fun assertLogs(expected: List<String>) {
        assertEquals(expected, logs.toList())
    }

    fun assertLogsContains(log: String) {
        assertTrue(
            """
            |Logs did not contain: $log
            |
            |Logs were:
            |${logs.joinToString("\n")}
            """.trimMargin()
        ) {
            logs.any { l -> l.contains(log) }
        }
    }

    fun assertEmptyLogs() {
        assertTrue(
            """
            |Logs were not empty.
            |
            |Logs were:
            |${logs.joinToString("\n")}
            """.trimMargin()
        ) {
            logs.isEmpty()
        }
    }
}
