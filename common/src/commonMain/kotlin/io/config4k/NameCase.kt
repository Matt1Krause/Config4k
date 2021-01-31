package io.config4k

sealed class NameCase {
    abstract fun computeNameFor(name: PropertyName): String

    object SnakeCase : NameCase() {
        override fun computeNameFor(name: PropertyName): String = name.joinToString(separator = "_")
    }

    object UpperSnakeCase : NameCase() {
        override fun computeNameFor(name: PropertyName): String =
            name.joinToString(separator = "_", transform = String::toUpperCase)
    }

    object PascalCase: NameCase() {
        override fun computeNameFor(name: PropertyName): String = name.joinToString(separator = "", transform = String::capitalize)
    }

    object CamelCase: NameCase() {
        override fun computeNameFor(name: PropertyName): String = PascalCase.computeNameFor(name).decapitalize()
    }

    object KebabCase: NameCase() {
        override fun computeNameFor(name: PropertyName): String = name.joinToString(separator = "-")
    }

    /**
     * [abc, def] -> abc.def
     */
    object InterDotted: NameCase() {
        override fun computeNameFor(name: PropertyName): String = name.joinToString(separator = ".")
    }

    object Raw: NameCase() {
        override fun computeNameFor(name: PropertyName): String = name.contentToString()
    }
}
