package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.logging.TestIO
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DependenciesTest {
    private val logger = TestIO()

    @Test
    fun `finding a dependency returns null when it is not found`() {
        val dependencies = Dependencies(logger)

        assertNull(dependencies.find("test"))
        logger.assertEmptyLogs()
    }

    @Test
    fun `finding a dependency returns it when it is a direct dependency`() {
        val dependencies = Dependencies(logger)
        dependencies.addDirectly("test")

        val expected = Dependency("test")
        val actual = dependencies.find("test")

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }

    @Test
    fun `finding a dependency returns it when it is a transitive dependency`() {
        val dependencies = Dependencies(logger)
        dependencies.addDirectly("test 1")
        dependencies.addTo("test 1", "test 2")

        val expected = Dependency("test 2")
        val actual = dependencies.find("test 2")

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }

    @Test
    fun `adding a direct dependency adds brand new direct dependency`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test")

        val expected = setOf(Dependency("test"))
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }

    @Test
    fun `adding a direct dependency skips already added direct dependency`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test")
        dependencies.addDirectly("test")

        val expected = setOf(Dependency("test"))
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertLogsContains("Dependency 'test' is already added as a direct dependency, skipping.")
    }

    @Test
    fun `adding a direct dependency that is a transitive dependency of something else adds it as direct dependency`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test 1")
        dependencies.addTo("test 1", "test 2")
        dependencies.addDirectly("test 2")

        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        test1.dependsOn(test2)

        val expected = setOf(test1, test2)
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }

    @Test
    fun `adding a dependency to an existing one fails when it depends on itself`() {
        val dependencies = Dependencies(logger)

        dependencies.addTo("test", "test")

        val expected = setOf<Dependency>()
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertLogsContains("Dependency 'test' cannot depend on itself!")
    }

    @Test
    fun `adding a dependency to an existing one fails when it is not found`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test 1")
        dependencies.addTo("test 2", "test 3")

        val expected = setOf(Dependency("test 1"))
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertLogsContains("Cannot make 'test 2' depend on 'test 3' because 'test 2' doesn't exist!")
    }

    @Test
    fun `adding a dependency to an existing one adds new dependency as brand new dependency when it is brand new`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test 1")
        dependencies.addTo("test 1", "test 2")

        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        test1.dependsOn(test2)

        val expected = setOf(test1)
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }

    @Test
    fun `adding a dependency to an existing one fails when there is a cyclic dependency`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test 1")
        dependencies.addTo("test 1", "test 2")
        dependencies.addDirectly("test 2")
        dependencies.addTo("test 2", "test 1")

        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        test1.dependsOn(test2)

        val expected = setOf(test1, test2)
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertLogsContains("Cyclic dependency found!")
        logger.assertLogsContains("Cannot make 'test 2' depend on 'test 1' because '[test 1 -> [test 2]]' depends on '[test 2]'")
    }

    @Test
    fun `adding a dependency to an existing one adds existing dependency to given one`() {
        val dependencies = Dependencies(logger)

        dependencies.addDirectly("test 1")
        dependencies.addTo("test 1", "test 2")
        dependencies.addDirectly("test 3")
        dependencies.addTo("test 1", "test 3")

        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        val test3 = Dependency("test 3")
        test1.dependsOn(test2)
        test1.dependsOn(test3)

        val expected = setOf(test1, test3)
        val actual = dependencies.dependencies()

        assertEquals(expected, actual)
        logger.assertEmptyLogs()
    }
}
