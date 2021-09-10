package dev.akif.dependencyfinder

import dev.akif.dependencyfinder.data.Dependencies
import dev.akif.dependencyfinder.data.Loader
import dev.akif.dependencyfinder.logging.ConsoleIO
import dev.akif.dependencyfinder.logging.IO

fun main(args: Array<String>) {
    val io = ConsoleIO

    if (args.size != 1) {
        return printHelp(args, io)
    }

    val file = args.first()
    val loader = Loader(file, io)

    loader.readFile()
        .flatMap { lines -> loader.parse(lines) }
        .map { dependencies -> loop(dependencies, io) }
        .getOrThrow()
}

fun loop(dependencies: Dependencies, io: IO) {
    io.print("Parsed ${dependencies.size()} dependencies.")
    while (true) {
        io.print("\nTo see transitive dependencies, enter name of a dependency or type \\q to quit.")
        when (val name = io.prompt("Name")) {
            "\\q" -> {
                io.print("Quitting")
                break
            }

            else -> {
                val dependency = dependencies.find(name)
                if (dependency == null) {
                    io.print("Dependency '$name' is not found!")
                } else {
                    io.print(dependency.list().joinToString(", ", "[", "]"))
                }
            }
        }
    }
}

fun printHelp(args: Array<String>, io: IO) {
    io.die(
        """
        |Invalid arguments:
        |${args.joinToString(" ")}
        |
        |You need to provide input file as a single program argument. Example when running with Gradle:
        |
        |gradle run --args '/path/to/dependencies.txt'
        """.trimMargin()
    )
}
