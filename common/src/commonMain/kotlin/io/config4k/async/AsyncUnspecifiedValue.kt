package io.config4k.async

sealed class AsyncUnspecifiedValue {
    abstract val value: Any
    class ByteValue(val byte: Byte): AsyncUnspecifiedValue() {
        override val value: Any
            get() = byte
    }
    //...
    class PropertyLoaderValue(private val asyncPropertyLoader: AsyncPropertyLoader): AsyncUnspecifiedValue(), AsyncPropertyLoader by asyncPropertyLoader {
        override val value: Any
            get() = asyncPropertyLoader

    }
}