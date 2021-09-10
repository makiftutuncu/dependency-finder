package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.firstMatching
import dev.akif.dependencyfinder.logging.Logger

class Dependencies(private val logger: Logger) {
    private val dependencies: MutableSet<Dependency> = mutableSetOf()

    fun find(name: String): Dependency? =
        dependencies.firstMatching { it.find(name) }

    fun add(fromName: String?, newName: String) {
        // Look for a reference to the dependency we want to add.
        val existingDependency = find(newName)

        if (fromName == null) {
            // We are adding a direct dependency.
            if (existingDependency == null) {
                // We did not find a reference to the dependency we want to add, it is a brand new one, add directly.
                val newDependency = Dependency(newName)
                dependencies.add(newDependency)
            } else {
                // We found a reference to the dependency we want to add, add directly.
                dependencies.add(existingDependency)
            }
        } else {
            // We are adding dependency to an existing one.
            val fromDependency = find(fromName)

            if (fromDependency == null) {
                logger.die("Cannot make '$fromName' depend on '$newName' because '$fromName' doesn't exist!")
            } else {
                // We have the reference to the dependency to which we want to add the new dependency.
                if (existingDependency == null) {
                    // We did not find a reference to the dependency we want to add, it is a brand new one, add transitively.
                    val newDependency = Dependency(newName)
                    fromDependency.dependsOn(newDependency)
                } else if (existingDependency.find(fromName) != null) {
                    val error = UnsupportedOperationException("Cannot make '$fromName' depend on '$newName' because '$existingDependency' depends on '$fromDependency'")
                    logger.die("Cyclic dependency found!", error)
                } else {
                    // We found a reference to the dependency we want to add, add transitively.
                    fromDependency.dependsOn(existingDependency)
                }
            }
        }
    }

    override fun toString(): String = dependencies.joinToString("\n")
}
