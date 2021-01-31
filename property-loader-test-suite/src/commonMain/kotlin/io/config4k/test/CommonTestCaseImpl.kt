package io.config4k.test

import io.config4k.MissingPropertyException
import io.config4k.SingleElementPropertyPath
import io.config4k.WrongPropertyTypeException
import io.config4k.asyncNested
import kotlin.math.pow
import kotlin.test.*

internal object PresentTestCase: TestCase {
    override val testDescription: String = "tests whether a stored value can be load successfully"

    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        val name = randomName
        val value = generate()
        propertiesBuilder.store(value, name)
        val returned = propertiesBuilder.get().load(name)
        assertTrue(
            value.valueEquals(returned),
            "Expected stored value ${value.valueToString()} and returned value ${value.valueToString()} of type $typeName to be equal"
        )
    }
}

internal object NotPresentTestCase: TestCase {
    override val testDescription: String = "tests whether load will fail on not stored value"
    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        assertFailsWith(
            MissingPropertyException::class,
            "Expected load$typeName to fail when load from empty properties"
        ) {
            propertiesBuilder.get().load(randomName)
        }
    }
}

internal object NamedPresentTestCase: TestCase {
    override val testDescription: String = "tests whether a value stored at a 1 depth location can be loaded after named"
    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        val nameAdvanced = randomName
        val name = randomName
        val value = generate()
        propertiesBuilder.advance(nameAdvanced).store(value, name)
        val returned = propertiesBuilder.get().asyncNested(nameAdvanced).load(name)
        assertTrue(
            value.valueEquals(returned),
            "Expected stored value ${value.valueToString()} and returned value ${value.valueToString()} of type$typeName to be equal, when after use of 'named'"
        )
    }
}

internal object NamedNotPresentTestCase: TestCase {
    override val testDescription: String = "tests whether load will fail "
    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        assertFailsWith(
            MissingPropertyException::class,
            "Expected load$typeName to fail when load from empty properties after use of 'named'"
        ) {
            propertiesBuilder.get().asyncNested(randomName).load(randomName)
        }
    }
}

internal object PropertyPathPresentTest: TestCase {
    override val testDescription: String
        get() = TODO("Not yet implemented")
    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        val nameAdvanced = randomName
        val name = randomName
        val value = generate()
        propertiesBuilder.advance(nameAdvanced).store(value, name)
        val returned = propertiesBuilder.get().load(SingleElementPropertyPath(nameAdvanced), name)
        assertTrue(
            value.valueEquals(returned),
            "Expected stored value ${value.valueToString()} and returned value ${value.valueToString()} of type$typeName to be equal, when loaded with path"
        )
    }
}

internal object PropertyPathNotPresentTest: TestCase {
    override val testDescription: String
        get() = TODO("Not yet implemented")

    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        assertFailsWith(
            MissingPropertyException::class,
            "Expected load$typeName to fail when load from empty properties with use of PropertyPath"
        ) {
            propertiesBuilder.get().load(SingleElementPropertyPath(randomName), randomName)
        }
    }
}

internal object FailOnStringLoad: TestCase {
    override val testDescription: String
        get() = TODO("Not yet implemented")

    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        val name = randomName
        val randomString: String = random.nextString(noNumberPool)
        propertiesBuilder.storeString(randomString, name)
        assertFailsWith(
            WrongPropertyTypeException::class,
            "Expected load$typeName to fail when load string $randomString"
        ) {
            propertiesBuilder.get().load(name)
        }
    }
}

internal object FailOnStringArrayLoad: TestCase {
    override val testDescription: String
        get() = TODO("Not yet implemented")

    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        val name = randomName
        val randomStringArray = Array(random.nextInt(1, 5)) {
            random.nextString(noNumberPool)
        }
        propertiesBuilder.storeStringArray(randomStringArray, name)
        assertFailsWith(
            WrongPropertyTypeException::class,
            "Expected load$typeName to fail when load string array ${randomStringArray.contentDeepToString()}"
        ) {
            propertiesBuilder.get().load(name)
        }
    }
}

internal class StringStressTest(private val charPool: List<Char>): TestCase {
    override val testDescription: String
        get() = TODO("Not yet implemented")

    override suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder) {
        repeat(4) {
            val length = 10.0.pow(it).toInt()
            val string = random.nextString(charPool, length, length + 1)
            val name = randomName
            propertiesBuilder.storeString(string, name)
            assertEquals(string, propertiesBuilder.get().loadString(name), "Expected loadString to succeed when loading a $length length string ")
        }
    }
}

fun <T> TypeTest<T>.commonTests(): TypeTest<T> {
    this += PresentTestCase
    this += NotPresentTestCase
    this +=  NamedPresentTestCase
    this += NamedNotPresentTestCase
    this += PropertyPathPresentTest
    this += PropertyPathNotPresentTest
    return this
}

fun <T> TypeTest<T>.numberTests(): TypeTest<T> {
    this += FailOnStringLoad
    return this
}

fun <T> TypeTest<T>.arrayTests(): TypeTest<T> {
    this += FailOnStringLoad
    return this
}

fun <T> TypeTest<T>.numberArrayTests(): TypeTest<T> {
    arrayTests()
    this += FailOnStringArrayLoad
    return this
}