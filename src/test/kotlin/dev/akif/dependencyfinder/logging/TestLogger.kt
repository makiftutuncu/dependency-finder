package dev.akif.dependencyfinder.logging

class TestLogger(val logs: MutableList<String> = mutableListOf()): Logger {
    override fun log(message: String) {
        logs.add(message)
    }

    override fun die(message: String, error: Throwable?) {
        logs.add(message)
        error?.message?.let { logs.add(it) }
    }
}
