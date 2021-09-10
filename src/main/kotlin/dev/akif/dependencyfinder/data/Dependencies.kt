package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.firstMatching
import dev.akif.dependencyfinder.logging.IO

class Dependencies(private val io: IO) {
    private val dependencies: MutableSet<Dependency> = mutableSetOf()

    fun find(name: String): Dependency? =
        dependencies.firstMatching { it.find(name) }

    fun addDirectly(newName: String) =
        add(null, newName)

    fun addTo(fromName: String, newName: String) =
        add(fromName, newName)

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

    fun size(): Int =
        dependencies.size

    override fun toString(): String = dependencies.joinToString("\n")
}
