package io.config4k.test

import io.config4k.PropertyName
import io.config4k.asyncNamed
import io.config4k.async.AsyncPropertyLoader
import kotlin.random.Random
import kotlin.test.assertTrue

class RandomTestData private constructor(
    random: Random,
    possibleTypes: List<TypeTest<*>>,
    nameGen: RandomNameGen,
    remainingDepth: Int
) {
    class TypeTestWithValue<T>(val test: TypeTest<T>, val value: T) {
        constructor(test: TypeTest<T>, random: Random) : this(test, with(test) { random.generate() })
    }

    constructor(random: Random, possibleTypes: List<TypeTest<*>>, nameGen: RandomNameGen) : this(
        random,
        possibleTypes,
        nameGen,
        random.nextInt(1, 7)
    )

    val children: List<Pair<PropertyName, RandomTestData>>
    val properties: List<Pair<PropertyName, TypeTestWithValue<*>>>

    init {
        val tmpChildren = ArrayList<Pair<PropertyName, RandomTestData>>()
        if (remainingDepth != 0)
            repeat(random.nextInt(1, 5)) {
                var name: PropertyName? = null
                while (name == null) {
                    name = nameGen.generate(random)
                    if (tmpChildren.any { it.first.contentEquals(name) })
                        name = null
                }
                tmpChildren += name to RandomTestData(
                    random,
                    possibleTypes,
                    nameGen,
                    remainingDepth - 1
                )
            }
        children = tmpChildren.toList()
        val tmpProperties = ArrayList<Pair<PropertyName, TypeTestWithValue<*>>>()
        repeat(random.nextInt(1, 7)) {
            val type = possibleTypes[random.nextInt(0, possibleTypes.size)]
            var name: PropertyName? = null
            while (name == null) {
                name = nameGen.generate(random)
                if (children.any {it.first.contentEquals(name)} || tmpProperties.any { it.first.contentEquals(name)})
                    name = null
            }
            tmpProperties += name!! to TypeTestWithValue(type, random)
        }
        properties = tmpProperties
    }

    suspend fun store(propertiesBuilder: PropertiesBuilder) {
        for (property in properties) {
            propertiesBuilder.store(property)
        }
        for (child in children) {
            child.second.store(propertiesBuilder.advance(child.first))
        }
    }

    private suspend fun PropertiesBuilder.store(property: Pair<PropertyName, TypeTestWithValue<*>>) {
        @Suppress("UNCHECKED_CAST")
        storeProperty(property as Pair<PropertyName, TypeTestWithValue<Any?>>)
    }

    private suspend fun <T> PropertiesBuilder.storeProperty(property: Pair<PropertyName, TypeTestWithValue<T>>) {
        val (name, typeTestValue) = property
        with(typeTestValue.test) {
            store(typeTestValue.value, name)
        }
    }

    suspend fun load(propertyLoader: AsyncPropertyLoader, path: List<PropertyName>) {
        for (property in properties) {
            propertyLoader.load(property, path)
        }
        for (child in children) {
            val (name, data) = child
            data.load(propertyLoader.asyncNamed(name), path.plusElement(name))
        }
    }

    suspend fun AsyncPropertyLoader.load(
        property: Pair<PropertyName, TypeTestWithValue<*>>,
        path: List<PropertyName>
    ) {
        @Suppress("UNCHECKED_CAST")
        loadProperty(property as Pair<PropertyName, TypeTestWithValue<Any?>>, path)
    }

    suspend fun <T> AsyncPropertyLoader.loadProperty(
        property: Pair<PropertyName, TypeTestWithValue<T>>,
        path: List<PropertyName>
    ) {
        val (name, typeTestValue) = property
        with(typeTestValue.test) {
            val loadedValue = load(name)
            assertTrue(
                loadedValue.valueEquals(typeTestValue.value),
                """Expected stored value ${typeTestValue.value.valueToString()}
                    | and loaded value ${loadedValue.valueToString()} 
                    | at path ${path.joinToString { it.contentToString() }}.${name.contentToString()}
                    | of type ${typeTestValue.test.typeName}
                    | to be equal""".trimMargin()
            )
        }
    }
}