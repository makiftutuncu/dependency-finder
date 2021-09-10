package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.logging.Logger
import java.io.File

class Loader(private val file: String, private val logger: Logger) {
    fun readFile(): Result<List<String>> =
        runCatching {
            File(file).readLines(Charsets.UTF_8)
        }.onFailure { e ->
            logger.die("Cannot read input file '$file'!", e)
        }

    fun parse(lines: List<String>): Result<Dependencies> =
        runCatching {
            logger.log("Parsing file '$file'")
            Dependencies(logger).apply {
                lines.forEach { line ->
                    val parts = line.split(" ")

                    if (parts.isNotEmpty()) {
                        val newName = parts.first()
                        val others = parts.drop(1)

                        addDirectly(newName)
                        others.forEach { name -> addTo(newName, name) }
                    }
                }
            }
        }.onFailure { e ->
            logger.die("Cannot parse input file '$file'!", e)
        }
}
