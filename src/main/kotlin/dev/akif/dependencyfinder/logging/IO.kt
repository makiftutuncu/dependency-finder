package dev.akif.dependencyfinder.logging

/**
 * An input/output interface for user interaction.
 */
interface IO {
    /**
     * Prints given message.
     *
     * @param message Message to print
     */
    fun print(message: String)

    /**
     * Prints given message (intended as error) and dies.
     *
     * @param message Message to print
     * @param error   Error to print if available
     */
    fun die(message: String, error: Throwable? = null)

    /**
     * Prints given prefix as a prompt and takes user input.
     *
     * @param prefix Message to print as prompt
     *
     * @return User input
     */
    fun prompt(prefix: String): String
}
