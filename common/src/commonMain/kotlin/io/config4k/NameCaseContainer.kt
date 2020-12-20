package io.config4k

class NameCaseContainer(private val name: PropertyName) {
    private var lSnakeCase: String? = null
    private inline val iSnakeCase: String
        get() {
            val current = lSnakeCase
            if (current == null) {
                val nName = NameCase.SnakeCase.computeNameFor(name)
                lSnakeCase = nName
                return nName
            }
            return current
        } 
    val snakeCase: String get() = iSnakeCase

    private var lUpperSnakeCase: String? = null
    private inline val iUpperSnakeCase: String
        get() {
            val current = lUpperSnakeCase
            if (current == null) {
                val nName = NameCase.UpperSnakeCase.computeNameFor(name)
                lUpperSnakeCase = nName
                return nName
            }
            return current
        }
    val upperSnakeCase: String get() = iUpperSnakeCase

    private var lPascalCase: String? = null
    private inline val iPascalCase: String
        get() {
            val current = lPascalCase
            if (current == null) {
                val nName = NameCase.PascalCase.computeNameFor(name)
                lPascalCase = nName
                return nName
            }
            return current
        }
    val pascalCase: String get() = iPascalCase

    private var lCamelCase: String? = null
    private inline val iCamelCase: String
        get() {
            val current = lCamelCase
            if (current == null) {
                val nName = NameCase.CamelCase.computeNameFor(name)
                lCamelCase = nName
                return nName
            }
            return current
        }
    val camelCase: String get() = iCamelCase

    private var lKebabCase: String? = null
    private inline val iKebabCase: String
        get() {
            val current = lKebabCase
            if (current == null) {
                val nName = NameCase.KebabCase.computeNameFor(name)
                lKebabCase = nName
                return nName
            }
            return current
        }
    val kebabCase: String get() = iKebabCase

    private var lInterDotted: String? = null
    private inline val iInterDotted: String
        get() {
            val current = lInterDotted
            if (current == null) {
                val nName = NameCase.InterDotted.computeNameFor(name)
                lInterDotted = nName
                return nName
            }
            return current
        }
    private val interDotted: String get() = iInterDotted

    fun computeNameFor(case: NameCase): String {
        return when (case) {
            NameCase.SnakeCase -> iSnakeCase
            NameCase.UpperSnakeCase -> iUpperSnakeCase
            NameCase.PascalCase -> iPascalCase
            NameCase.CamelCase -> iCamelCase
            NameCase.KebabCase -> iKebabCase
            NameCase.InterDotted -> iInterDotted
        }
    }
}