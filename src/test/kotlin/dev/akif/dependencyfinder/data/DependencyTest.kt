package dev.akif.dependencyfinder.data

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DependencyTest {
    @Test
    fun `finding a dependency returns null when it is not found`() {
        val dependency = Dependency("test 1")

        assertNull(dependency.find("test 2"))
    }

    @Test
    fun `finding a dependency returns itself when queried`() {
        val dependency = Dependency("test")

        val expected = dependency
        val actual = dependency.find("test")

        assertEquals(expected, actual)
    }

    @Test
    fun `finding a dependency returns a transitive dependency`() {
        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        test1.dependsOn(test2)

        val expected = test2
        val actual = test1.find("test 2")

        assertEquals(expected, actual)
    }

    @Test
    fun `listing dependencies returns `() {
        val test1 = Dependency("test 1")
        val test2 = Dependency("test 2")
        val test3 = Dependency("test 3")
        val test4 = Dependency("test 4")
        test1.dependsOn(test2)
        test1.dependsOn(test3)
        test2.dependsOn(test4)
        test3.dependsOn(test4)

        val expected = setOf("test 2", "test 3", "test 4")
        val actual = test1.list()

        assertEquals(expected, actual)
    }

    @Test
    fun `making dependency depend on another adds a transitive dependency`() {
        val test1 = Dependency("test 1")

        assertEquals(setOf(), test1.list())

        val test2 = Dependency("test 2")
        test1.dependsOn(test2)

        assertEquals(setOf("test 2"), test1.list())
    }
}
