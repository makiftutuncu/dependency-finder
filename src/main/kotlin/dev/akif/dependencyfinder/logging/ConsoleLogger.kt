package dev.akif.dependencyfinder.logging

import kotlin.system.exitProcess

object ConsoleLogger: Logger {
    override fun log(message: String) = println(message)

    override fun die(message: String, error: Throwable?) {
        println(message)
        error?.run { printStackTrace() }
        exitProcess(1)
    }
}
