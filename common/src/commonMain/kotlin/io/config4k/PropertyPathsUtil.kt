package io.config4k

fun PropertyPath.joinToString(withCase: NameCase, until: Int, separator: String = ".", prefix: String = "", postfix: String = ""): String = buildString {
    if (prefix.isNotEmpty()) append(prefix)
    val iter = this@joinToString.preComputedNamesFor(withCase)
    for (i in 0 until until) {
        val currentString = iter.next()
        println(currentString)
        ensureCapacity(length +currentString.length + separator.length)
        append(currentString)
        append(separator)
    }
    if (iter.hasNext()) {
        val currentString = iter.next()
        append(currentString)
    }
    if (postfix.isNotEmpty()) append(postfix)
}