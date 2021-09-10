package dev.akif.dependencyfinder.data

import dev.akif.dependencyfinder.firstMatching

data class Dependency(val name: String) {
    private val dependencies: MutableSet<Dependency> = mutableSetOf()

    fun find(name: String): Dependency? =
        if (this.name == name) {
            this
        } else {
            dependencies.firstMatching { it.find(name) }
        }

    fun list(): Set<String> =
        mutableSetOf<String>().apply {
            dependencies.forEach { dependency ->
                add(dependency.name)
                addAll(dependency.list())
            }
        }

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
