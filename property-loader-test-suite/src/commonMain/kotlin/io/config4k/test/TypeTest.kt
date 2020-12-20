package io.config4k.test

import io.config4k.PropertyName
import io.config4k.PropertyPath
import io.config4k.async.AsyncPropertyLoader
import kotlin.random.Random

abstract class TypeTest<T>(
    val typeName: String,
    val nameGen: RandomNameGen,
    random: Random
) {
    private val tests: MutableList<TestCase> = arrayListOf()
    val random = Random(random.nextLong())

    operator fun plusAssign(test: TestCase) {
        tests.add(test)
    }

    abstract fun Random.generate(): T
    abstract suspend fun propertiesBuilder(): PropertiesBuilder
    abstract suspend fun PropertiesBuilder.store(value: T, name: PropertyName)
    abstract suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): T
    abstract suspend fun AsyncPropertyLoader.load(name: PropertyName): T
    open fun T.valueEquals(other: Any?): Boolean = this == other
    open fun T.valueToString(): String = toString()

    val randomName: PropertyName get() = nameGen.generate(random)
    fun generate(): T = random.generate()

    suspend fun execute() {
        for (test in tests) {
            with(test) {
                test(propertiesBuilder())
            }
        }
    }
}