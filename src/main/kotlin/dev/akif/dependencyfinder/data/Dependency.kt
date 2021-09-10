package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.firstMatching

/**
 * Represents a dependency.
 *
 * @param name Name of the dependency
 */
data class Dependency(val name: String) {
    private val dependencies: MutableSet<Dependency> = mutableSetOf()

    /**
     * Finds a direct or transitive dependency with given name.
     *
     * @param name Name of the dependency to find
     *
     * @return Found dependency or null if it is not found
     */
    fun find(name: String): Dependency? =
        if (this.name == name) {
            this
        } else {
            dependencies.firstMatching { it.find(name) }
        }

    /**
     * Lists transitive dependencies of this dependency.
     *
     * @return Transitive dependencies of this dependency as a set
     */
    fun list(): Set<String> =
        mutableSetOf<String>().apply {
            dependencies.forEach { dependency ->
                add(dependency.name)
                addAll(dependency.list())
            }
        }

    /**
     * Makes this dependency depend transitively on given dependency.
     *
     * @param existing An existing dependency to depend on transitively
     */
    fun dependsOn(existing: Dependency) {
        dependencies.add(existing)
    }

    override fun toString(): String =
        if (dependencies.isEmpty()) {
            "[$name]"
        } else {
            "[$name -> ${dependencies.joinToString(", ")}]"
        }
}
