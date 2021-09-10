package dev.akif.dependencyfinder

import dev.akif.dependencyfinder.logging.ConsoleLogger
import dev.akif.dependencyfinder.data.Dependencies
import dev.akif.dependencyfinder.data.Loader

val logger = ConsoleLogger

fun main(args: Array<String>) {
    if (args.size != 1) {
        return printHelp(args)
    }

    val file = args.first()
    val loader = Loader(file, logger)

    loader.readFile()
        .flatMap { lines -> loader.parse(lines) }
        .map { dependencies -> loop(dependencies) }
        .getOrThrow()
}

fun loop(dependencies: Dependencies) {
    println("Parsed dependencies:\n$dependencies")
}

fun printHelp(args: Array<String>) {
    logger.die("""
        Invalid arguments:
        ${args.joinToString(" ")}
        
        You need to provide input file as a single program argument. Example when running with Gradle:
        
        gradle run --args '/path/to/dependencies.txt'
    """.trimIndent())
}
