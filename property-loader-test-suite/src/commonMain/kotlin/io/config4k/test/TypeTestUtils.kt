package io.config4k.test

import io.config4k.PropertyName
import io.config4k.PropertyPath
import io.config4k.async.AsyncPropertyLoader
import kotlin.random.Random
import kotlin.random.nextUInt

internal inline fun <reified T> T.customEquals(other: Any?, customEquals: T.(T)->Boolean): Boolean {
    if (other == null || other !is T) return false
    return customEquals(other)
}

fun interface RandomNameGen {
    fun generate(random: Random): PropertyName
}

private val defaultFromPool = ('a'..'z') + ('A'..'Z') + ('1'..'9')
val noNumberPool = (('a'..'z') + ('A'..'Z'))
fun Random.nextString(fromPool: List<Char> = defaultFromPool, minSize: Int = 1, maxSize: Int = 5): String {
    return CharArray(nextInt(minSize, maxSize)) {
        fromPool[nextInt(0, fromPool.size - 1)]
    }.concatToString()
}
val numberRegex = Regex("\\d+")

inline fun <T, U> Random.generateArray(get: (Int, (Int)->U)->T, crossinline generate: Random.()->U): T {
    return get(nextInt(1, 5)) {generate()}
}

inline fun <T, U> Random.generateArray(get: (Int, (Int)->U)->T, minLen: Int = 1, maxLen: Int = 5, crossinline generate: Random.()->U): T {
    return get(nextInt(minLen, maxLen)) {generate()}
}

fun Random.nextByte() = nextBits(8).toByte()
fun Random.nextShort() = nextBits(16).toShort()
@ExperimentalUnsignedTypes
fun Random.nextUByte() = nextUInt().toUByte()
@ExperimentalUnsignedTypes
fun Random.nextUShort() = nextUInt().toUShort()

class StringTypeTest(
    private val pool: List<Char>,
    nameGen: RandomNameGen,
    random: Random,
    private val minLength: Int = 1,
    private val maxLength: Int = 5,
    private val getPropertiesBuilder: suspend ()-> PropertiesBuilder
)
    : TypeTest<String>("String", nameGen, random) {
    override fun Random.generate(): String = nextString(pool, minLength, maxLength)
    override suspend fun PropertiesBuilder.store(value: String, name: PropertyName) = storeString(value, name)
    override suspend fun AsyncPropertyLoader.load(name: PropertyName): String = loadString(name)
    override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): String =
        loadString(propertyPath, name)

    override suspend fun propertiesBuilder(): PropertiesBuilder = getPropertiesBuilder()
}

class StringArrayTypeTest(
    private val pool: List<Char>,
    nameGen: RandomNameGen,
    random: Random,
    private val minStringLength: Int = 1,
    private val maxStringLength: Int = 10,
    private val minArrayLen: Int = 1,
    private val maxArrayLen: Int = 10,
    private val getPropertiesBuilder: suspend () -> PropertiesBuilder
): TypeTest<Array<String>>("StringArray", nameGen, random) {
    override fun Random.generate(): Array<String> = generateArray(::Array, minArrayLen, maxArrayLen) {
        nextString(pool, minStringLength, maxStringLength)
    }
    override suspend fun PropertiesBuilder.store(value: Array<String>, name: PropertyName)=
        storeStringArray(value, name)

    override suspend fun AsyncPropertyLoader.load(name: PropertyName): Array<String> = loadStringArray(name)
    override suspend fun AsyncPropertyLoader.load(
        propertyPath: PropertyPath,
        name: PropertyName
    ): Array<String> = loadStringArray(propertyPath, name)

    override fun Array<String>.valueEquals(other: Any?): Boolean = customEquals(other, Array<String>::contentDeepEquals)
    override fun Array<String>.valueToString(): String = contentDeepToString()

    override suspend fun propertiesBuilder(): PropertiesBuilder = getPropertiesBuilder()
}