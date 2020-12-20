package io.config4k.json

fun PropertyTree.Companion.from(string: String, mapSupplier: MapSupplier = MapSupplier.HashMap): PropertyTree {
    var state = 0
    //state = 0 -> whitespaces before property
    //state = 1 -> comment
    //state = 2 .-> property
    //state = 3 -> escaped property
    //
    val rootTree = PropertyTree(mapSupplier, null)
    var current = rootTree
    val currentString = StringBuilder()
    val currentUnicode = CharArray(4)
    for (c in string) {
        when (state) {
            0 -> if (!c.isWhitespace())
                when (c) {
                    '!', '#' -> state = 1
                    '.' -> state = 2
                    else -> {
                        state = 2; currentString.append(c)
                    }
                }
            1 -> if (c == '\n') {
                state = 0
                current = rootTree
            }
            2 -> when (c) {
                '\\' -> state = 3
                '\n' -> {
                    val currentStringString = currentString.toString()
                    if (currentStringString.isNotBlank()) current = current.getOrCreate(currentStringString)
                    if (current.value != null) throw IllegalArgumentException("Duplicate property: $currentStringString")
                    current.value = ""
                    state = 0
                    currentString.clear()
                    current = rootTree
                }
                '.' -> {
                    current = current.setTo(currentString)
                }
                ' ', '\t' -> {
                    current = current.setTo(currentString)
                    state = 4//todo change?
                }
                '=' -> {
                    current = current.setTo(currentString)
                    state = 5
                }
                else -> currentString.append(c)
            }
            3 -> when (c) {
                '\\' -> {
                    currentString.append('\\')
                    state = 2
                }
                '\n' -> {
                    val currentStringString = currentString.toString()
                    if (currentStringString.isNotBlank()) current = current.getOrCreate(currentStringString)
                    if (current.value != null) throw IllegalArgumentException("Duplicate property: $currentStringString")
                    current.value = ""
                    state = 0
                    currentString.clear()
                    current = rootTree
                }
                ' ', '\t' -> {
                    current.setTo(currentString)
                    state = 4//fixme
                }
                '=' -> {
                    currentString.append('=')
                    state = 2
                }
                'n' -> {
                    currentString.append('\n')
                    state = 2
                }
                't' -> {
                    currentString.append('\t')
                    state = 2
                }
                'r' -> {
                    currentString.append("\r")
                    state = 2
                }
                'u' -> state = 4
                else -> {
                    currentString.append(c)
                    state = 2
                }
            }
            4 -> {
                if (c.isValidHexadecimal()) {
                    currentUnicode[0] = c
                    state = 5
                } else
                    throw IllegalArgumentException("Malformed \\uxxxx at $currentString")
            }
            5 -> {
                if (c.isValidHexadecimal()) {
                    currentUnicode[1] = c
                    state = 6
                } else
                    throw IllegalArgumentException("Malformed \\uxxxx at $currentString")
            }
            6 -> {
                if (c.isValidHexadecimal()) {
                    currentUnicode[2] = c
                    state = 7
                } else
                    throw IllegalArgumentException("Malformed \\uxxxx at $currentString")
            }
            7 -> {
                if (c.isValidHexadecimal()) {
                    currentUnicode[3] = c
                    state = 8
                } else
                    throw IllegalArgumentException("Malformed \\uxxxx at $currentString")
            }
        }
    }
    TODO()
}

private inline fun Char.isValidHexadecimal(): Boolean =
    when (this) {
        'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> true
        else -> false
    }

private inline fun PropertyTree.setTo(stringBuilder: StringBuilder): PropertyTree {
    val asString = stringBuilder.toString()
    stringBuilder.clear()
    return if (asString.isNotBlank()) getOrCreate(asString) else this
}
