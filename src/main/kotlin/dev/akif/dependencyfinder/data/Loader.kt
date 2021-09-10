package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.logging.IO
import java.io.File

/**
 * Responsible for loading dependencies from a file.
 */
class Loader(private val file: String, private val io: IO) {
    /**
     * Reads input file of dependency definitions.
     *
     * @return List of lines in the file as a Result
     */
    fun readFile(): Result<List<String>> =
        runCatching {
            File(file).readLines(Charsets.UTF_8)
        }.onFailure { e ->
            io.die("Cannot read input file '$file'!", e)
        }

    /**
     * Parses given lines as dependency definitions.
     *
     * @param lines Lines read from the file
     *
     * @return Parsed dependencies as a Result
     */
    fun parse(lines: List<String>): Result<Dependencies> =
        runCatching {
            io.print("Parsing file '$file'")
            Dependencies(io).apply {
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
            io.die("Cannot parse input file '$file'!", e)
        }
}
