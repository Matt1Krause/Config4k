package io.config4k.json

interface MapSupplier {
    object HashMap: MapSupplier {
        override fun <T, U> get(): MutableMap<T, U> = HashMap()
    }
    fun <T, U> get(): MutableMap<T, U>
}