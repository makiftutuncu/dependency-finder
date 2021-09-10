package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.logging.TestIO
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoaderTest {
    private val logger = TestIO()

    @Test
    fun `reading file fails when file is not found`() {
        val loader = Loader("invalidfile.txt", logger)

        val result = loader.readFile()

        assertNull(result.getOrNull())
        assertEquals("invalidfile.txt (No such file or directory)", result.exceptionOrNull()?.message)
        logger.assertLogsContains("Cannot read input file 'invalidfile.txt'!")
    }

    @Test
    fun `reading file returns lines of file`() {
        val loader = Loader("dependencies.txt", logger)

        val result = loader.readFile()

        val expected = listOf(
            "A B C",
            "B C E",
            "C G",
            "D A F",
            "E F",
            "F H"
        )

        assertNull(result.exceptionOrNull())
        assertEquals(expected, result.getOrNull())
        logger.assertEmptyLogs()
    }

    @Test
    fun `parsing lines returns dependencies`() {
        val loader = Loader("dependencies.txt", logger)

        val lines = listOf(
            "A B",
            "B C"
        )

        val result = loader.parse(lines)

        val dependencies = Dependencies(logger)
        dependencies.addDirectly("A")
        dependencies.addTo("A", "B")
        dependencies.addDirectly("B")
        dependencies.addTo("B", "C")

        assertNull(result.exceptionOrNull())
        assertEquals(dependencies.dependencies(), result.getOrNull()?.dependencies())
        logger.assertLogsContains("Parsing file 'dependencies.txt'")
    }
}
