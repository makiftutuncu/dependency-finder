package dev.akif.dependencyfinder.logging

interface IO {
    fun print(message: String)
    fun die(message: String, error: Throwable? = null)
    fun prompt(prefix: String): String
}
