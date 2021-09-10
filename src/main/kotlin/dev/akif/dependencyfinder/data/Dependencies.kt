package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.firstMatching
import dev.akif.dependencyfinder.logging.IO

/**
 * Represents a dependency graph.
 */
class Dependencies(private val io: IO) {
    private val dependencies: MutableSet<Dependency> = mutableSetOf()

    /**
     * Finds a direct or transitive dependency with given name in the graph.
     *
     * @param name Name of the dependency to find
     *
     * @return Found dependency or null if it is not found
     */
    fun find(name: String): Dependency? =
        dependencies.firstMatching { it.find(name) }

    /**
     * Adds a direct dependency to the graph with given name.
     *
     * @param newName Name of the dependency to add
     */
    fun addDirectly(newName: String): Unit =
        add(null, newName)

    /**
     * Adds a transitive dependency to given dependency in the graph with given name.
     *
     * @param fromName Name of the dependency to which to add the new dependency
     * @param newName  Name of the dependency to add
     */
    fun addTo(fromName: String, newName: String): Unit =
        add(fromName, newName)

    /**
     * Gets added direct dependencies as a set.
     *
     * @return Added direct dependencies as a set
     */
    fun dependencies(): Set<Dependency> =
        dependencies.toSet()

    private fun add(fromName: String?, newName: String) {
        // Look for a reference to the dependency we want to add.
        val existingDependency = find(newName)

        if (fromName == null) {
            // We are adding a direct dependency.
            if (existingDependency == null) {
                // We did not find a reference to the dependency we want to add, it is a brand new one, add directly.
                val newDependency = Dependency(newName)
                dependencies.add(newDependency)
            } else {
                // We found a reference to the dependency we want to add.
                if (dependencies.any { dependency -> dependency.name == newName }) {
                    // The reference we found is a direct dependency already.
                    io.print("Dependency '$newName' is already added as a direct dependency, skipping.")
                } else {
                    // The reference we found is a transitive dependency of something else so add it as a direct dependency.
                    dependencies.add(existingDependency)
                }
            }
        } else if (fromName == newName) {
            io.die("Dependency '$fromName' cannot depend on itself!")
        } else {
            // We are adding dependency to an existing one.
            val fromDependency = find(fromName)

            if (fromDependency == null) {
                io.die("Cannot make '$fromName' depend on '$newName' because '$fromName' doesn't exist!")
            } else {
                // We have the reference to the dependency to which we want to add the new dependency.
                if (existingDependency == null) {
                    // We did not find a reference to the dependency we want to add, it is a brand new one, add transitively.
                    val newDependency = Dependency(newName)
                    fromDependency.dependsOn(newDependency)
                } else if (existingDependency.find(fromName) != null) {
                    val error = UnsupportedOperationException("Cannot make '$fromName' depend on '$newName' because '$existingDependency' depends on '$fromDependency'")
                    io.die("Cyclic dependency found!", error)
                } else {
                    // We found a reference to the dependency we want to add, add transitively.
                    fromDependency.dependsOn(existingDependency)
                }
            }
        }
    }

    /**
     * Gets number of direct dependencies.
     *
     * @return Number of direct dependencies
     */
    fun size(): Int =
        dependencies.size

    override fun toString(): String = dependencies.joinToString("\n")
}
