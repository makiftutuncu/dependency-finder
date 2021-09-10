package dev.akif.dependencyfinder

/**
 * Finds first available item in the collection.
 *
 * @param get Function to get the resulting value
 * @param A   Type of items in collection
 * @param B   Type of resulting item
 *
 * @return Found matching item or null if not found
 */
fun <A, B> Collection<A>.firstMatching(get: (A) -> B?): B? {
    for (a in this) {
        val b = get(a)
        if (b != null) {
            return b
        }
    }
    return null
}

/**
 * Runs given action that can fail with the value of this result (if successful).
 *
 * @param transform Action to perform
 * @param A         Type of value in this Result
 * @param B         Type of Result after performing action
 *
 * @return Result containing value after performing action if successful or resulting failure
 */
fun <A, B> Result<A>.flatMap(transform: (A) -> Result<B>): Result<B> {
    return getOrNull()?.let { a -> transform(a) } ?: Result.failure(exceptionOrNull()!!)
}
