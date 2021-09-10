package dev.akif.dependencyfinder.logging

interface Logger {
    fun log(message: String)
    fun die(message: String, error: Throwable? = null)
}
