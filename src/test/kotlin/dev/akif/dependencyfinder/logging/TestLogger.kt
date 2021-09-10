package dev.akif.dependencyfinder.logging

import kotlin.test.assertTrue

class TestLogger: Logger {
    val logs: MutableList<String> = mutableListOf()

    override fun log(message: String) {
        logs.add(message)
    }

    override fun die(message: String, error: Throwable?) {
        logs.add(message)
        error?.message?.let { logs.add(it) }
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
