package dev.akif.dependencyfinder.logging

import java.util.Scanner
import kotlin.system.exitProcess

object ConsoleIO: IO {
    private val scanner = Scanner(System.`in`)

    override fun print(message: String) =
        println(message)

    override fun die(message: String, error: Throwable?) {
        println(message)
        error?.run { printStackTrace() }
        exitProcess(1)
    }

    override fun prompt(prefix: String): String {
        kotlin.io.print("$prefix: ")
        return scanner.nextLine().trim()
    }
}
