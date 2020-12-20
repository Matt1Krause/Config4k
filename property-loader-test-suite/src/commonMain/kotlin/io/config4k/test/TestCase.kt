package io.config4k.test

interface TestCase {
    val testDescription: String
    suspend fun <T> TypeTest<T>.test(propertiesBuilder: PropertiesBuilder)
}
