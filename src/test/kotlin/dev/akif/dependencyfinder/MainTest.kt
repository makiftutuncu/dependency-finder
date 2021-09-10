package dev.akif.dependencyfinder

import dev.akif.dependencyfinder.data.Dependencies
import dev.akif.dependencyfinder.logging.TestIO
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `looping with an example dependency graph`() {
        val io = TestIO()
        val dependencies = Dependencies(io)
        dependencies.addDirectly("A")
        dependencies.addTo("A", "B")
        dependencies.addTo("A", "C")
        dependencies.addDirectly("B")
        dependencies.addTo("B", "C")
        dependencies.addTo("B", "E")
        dependencies.addDirectly("C")
        dependencies.addTo("C", "G")
        dependencies.addDirectly("D")
        dependencies.addTo("D", "A")
        dependencies.addTo("D", "F")
        dependencies.addDirectly("E")
        dependencies.addTo("E", "F")
        dependencies.addDirectly("F")
        dependencies.addTo("F", "H")

        io.input("Q")
        io.input("A")
        io.input("C")
        io.input("\\q")

        loop(dependencies, io)

        val expectedLogs = listOf(
            "Parsed 6 dependencies.",
            "\nTo see transitive dependencies, enter name of a dependency or type \\q to quit.",
            "Dependency 'Q' is not found!",
            "\nTo see transitive dependencies, enter name of a dependency or type \\q to quit.",
            "[B, C, G, E, F, H]",
            "\nTo see transitive dependencies, enter name of a dependency or type \\q to quit.",
            "[G]",
            "\nTo see transitive dependencies, enter name of a dependency or type \\q to quit.",
            "Quitting"
        )

        io.assertLogs(expectedLogs)
    }
}
