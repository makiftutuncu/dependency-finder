package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.logging.IO
import java.io.File

class Loader(private val file: String, private val io: IO) {
    fun readFile(): Result<List<String>> =
        runCatching {
            File(file).readLines(Charsets.UTF_8)
        }.onFailure { e ->
            io.die("Cannot read input file '$file'!", e)
        }

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
