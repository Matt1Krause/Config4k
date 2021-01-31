@file:Suppress("NOTHING_TO_INLINE")

package io.config4k

abstract class InvalidPropertyException : Exception() {
    abstract override val message: String
}

abstract class LazyInvalidPropertyException : InvalidPropertyException() {
    private var lazyMessage: String? = null
    override val message: String
        get() {
            if (lazyMessage == null)
                lazyMessage = computeMessage()
            return lazyMessage!!
        }

    protected abstract fun computeMessage(): String
}

class InvalidPropertyExceptionImpl(override val message: String) : InvalidPropertyException()
class LazyPathInvalidPropertyException(
    private val propertyPath: PropertyPath,
    private val until: Int,
    private val case: NameCase,
    private val messageBefore: String,
    private val messageAfter: String
) : InvalidPropertyException() {
    private var lazyMessage: String? = null
    override val message: String
        get() {
            if (lazyMessage == null) {
                lazyMessage = buildString(messageBefore.length + messageAfter.length + (until + 1) * 5) {
                    val iter = propertyPath.preComputedNamesFor(case)
                    append(messageBefore)
                    for (i in 0 until until) {
                        append(iter.next())
                        append('.')
                    }
                    if (iter.hasNext()) append(iter.next())
                    append(messageAfter)
                }
            }
            return lazyMessage!!
        }
}

abstract class MissingPropertyException : LazyInvalidPropertyException()

class LazyMissingPropertyExceptionImpl(
    private val propertyPath: PropertyPath,
    private val pathEnd: PropertyName?,
    private val case: NameCase,
    private val expected: String,
    private val until: Int = propertyPath.length,
    private val an: Boolean = false,
    private val withIndex: Int? = null
) : MissingPropertyException() {
    override fun computeMessage(): String {
        val pathExtension = pathEnd?.let { ".${case.computeNameFor(it)}" } ?: "" + withIndex?.let { ".$it" }
        val aOrAn = if (an) "an" else "a"
        return propertyPath.joinToString(
            case,
            until,
            prefix = "expected $aOrAn $expected at ",
            postfix = "$pathExtension but got nothing"
        )
    }
}

class CustomMessageMissingPropertyException(private val theMessage: String) : MissingPropertyException() {
    override fun computeMessage(): String = theMessage
}

abstract class WrongPropertyTypeException : LazyInvalidPropertyException()

abstract class AbstractWrongPropertyTypeException(
    private val propertyPath: PropertyPath,
    private val pathEnd: PropertyName?,
    private val case: NameCase,
    private val expected: String,
    private val until: Int = propertyPath.length,
    private val an: Boolean = false,
    private val withIndex: Int? = null
) : WrongPropertyTypeException() {
    protected abstract val got: String
    override fun computeMessage(): String {
        val pathExtension = pathEnd?.let { case.computeNameFor(it) } ?: "" + withIndex?.let { ".$it" }
        val aOrAn = if (an) "an" else "a"
        return propertyPath.joinToString(
            case,
            until,
            prefix = "expected $aOrAn $expected at ",
            postfix = "$pathExtension but got $got"
        )
    }
}

class WrongPropertyTypeExceptionImpl(
    propertyPath: PropertyPath,
    pathEnd: PropertyName?,
    case: NameCase,
    expected: String,
    override val got: String,
    until: Int = propertyPath.length,
    an: Boolean = false,
    withIndex: Int? = null
) : AbstractWrongPropertyTypeException(propertyPath, pathEnd, case, expected, until, an, withIndex)

class LazyWrongPropertyTypeExceptionImpl(
    propertyPath: PropertyPath,
    pathEnd: PropertyName?,
    case: NameCase,
    expected: String,
    until: Int = propertyPath.length,
    an: Boolean = false,
    withIndex: Int? = null,
    private val gotImpl: () -> String
) : AbstractWrongPropertyTypeException(propertyPath, pathEnd, case, expected, until, an, withIndex) {
    override val got: String
        get() = gotImpl()
}

class CustomMessageWrongPropertyException(private val theMessage: String) : WrongPropertyTypeException() {
    override fun computeMessage(): String = theMessage
}

class MutablePathWrongPropertyException(
    private val mutablePath: MutableList<PropertyName>,
    private val case: NameCase,
    private val expected: String,
    private val an: Boolean = false,
    private val gotImpl: () -> String
) : WrongPropertyTypeException() {
    fun append(propertyName: PropertyName) {
        mutablePath.add(propertyName)
    }

    override fun computeMessage(): String {
        val aOrAn = if (an) "an" else "a"
        return mutablePath.joinToString(
            separator = ".",
            prefix = "expected $aOrAn $expected at",
            postfix = " but got ${gotImpl.invoke()}"
        ) {
            case.computeNameFor(it)
        }
    }
}

inline fun WrongPropertyTypeException(
    propertyPath: PropertyPath,
    pathEnd: PropertyName?,
    case: NameCase,
    expected: String,
    got: String,
    until: Int = propertyPath.length,
    an: Boolean = false,
    withIndex: Int? = null
): WrongPropertyTypeException =
    WrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, expected, got, until, an, withIndex)

inline fun WrongPropertyTypeException(
    propertyPath: PropertyPath,
    pathEnd: PropertyName?,
    case: NameCase,
    expected: String,
    until: Int = propertyPath.length,
    an: Boolean = false,
    withIndex: Int? = null,
    noinline gotImpl: () -> String
): WrongPropertyTypeException =
    LazyWrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, expected, until, an, withIndex, gotImpl)

inline fun WrongPropertyTypeException(message: String): WrongPropertyTypeException =
    CustomMessageWrongPropertyException(message)

inline fun MissingPropertyException(
    propertyPath: PropertyPath,
    pathEnd: PropertyName?,
    case: NameCase,
    expected: String,
    until: Int = propertyPath.length,
    an: Boolean = false,
    withIndex: Int? = null
): MissingPropertyException =
    LazyMissingPropertyExceptionImpl(propertyPath, pathEnd, case, expected, until, an, withIndex)

inline fun MissingPropertyException(message: String): MissingPropertyException =
    CustomMessageMissingPropertyException(message)