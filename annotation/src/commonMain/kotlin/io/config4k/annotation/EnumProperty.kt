package io.config4k.annotation

annotation class EnumProperty(val representation: Representation = Representation.NAME) {
    enum class Representation {
        NAME, ORDINAL
    }
    companion object {
        val REPRESENTATION = Representation.NAME
    }
}
