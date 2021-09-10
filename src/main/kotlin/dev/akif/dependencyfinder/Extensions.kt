package dev.akif.dependencyfinder

fun <A, B> Collection<A>.firstMatching(get: (A) -> B?): B? {
    for (a in this) {
        val b = get(a)
        if (b != null) {
            return b
        }
    }
    return null
}

fun <A, B> Result<A>.flatMap(transform: (A) -> Result<B>): Result<B> {
    return getOrNull()?.let { a -> transform(a) } ?: Result.failure(exceptionOrNull()!!)
}
