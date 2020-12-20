import io.config4k.NameCase
import io.config4k.PropertyName
import io.config4k.async
import io.github.config4k.*
import io.config4k.async.AsyncPropertyLoader
import io.config4k.json.JsonPropertyLoader
import io.config4k.test.CommonNumberPropertiesBuilder
import io.config4k.test.PropertiesBuilder
import io.config4k.test.Tester
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Suppress("EXPERIMENTAL_API_USAGE")
class TesterImpl : Tester() {
    private val case = NameCase.SnakeCase
    private val factory = JsonPropertyLoader.Factory(case)
    override fun propertyBuilder(): PropertiesBuilder {
        return PropertiesBuilderImpl(case, factory)
    }

    private class PropertiesBuilderImpl(
        private val case: NameCase,
        val factory: JsonPropertyLoader.Factory
        ) : CommonNumberPropertiesBuilder {
        private val namedProperty: NamedProperty = NamedProperty()
        private abstract class Property {
            abstract fun toJson(): JsonElement
        }

        private inner class NamedProperty : Property() {
            val properties: MutableList<Pair<PropertyName, Property>> = ArrayList()
            override fun toJson(): JsonElement {
                val map = hashMapOf<String, JsonElement>()
                properties.forEach {
                    map[case.computeNameFor(it.first)] = it.second.toJson()
                }
                return JsonObject(map)
            }
        }

        private inner class EnumeratedProperty(val values: List<SimpleProperty>) : Property() {
            override fun toJson(): JsonElement {
                return JsonArray(values.map { it.toJson() })
            }
        }
        private inner class SimpleProperty(val value: JsonPrimitive) : Property() {
            override fun toJson(): JsonElement {
                return value
            }
        }

        override fun storeNumber(number: Number, name: PropertyName) {
            namedProperty.properties.add(name to SimpleProperty(JsonPrimitive(number)))
        }

        override fun storeULong(uLong: ULong, name: PropertyName) {
            namedProperty.properties.add(name to SimpleProperty(JsonPrimitive(uLong.toString())))
        }

        override fun storeNumberArray(numberArray: Array<Number>, name: PropertyName) {
            namedProperty.properties.add(name to EnumeratedProperty(numberArray.map { SimpleProperty(JsonPrimitive(it)) }))
        }

        override fun storeBoolean(boolean: Boolean, name: PropertyName) {
            namedProperty.properties.add(name to SimpleProperty(JsonPrimitive(boolean)))
        }

        override fun storeString(string: String, name: PropertyName) {
            namedProperty.properties.add(name to SimpleProperty(JsonPrimitive(string)))
        }

        override fun storeBooleanArray(booleanArray: BooleanArray, name: PropertyName) {
            namedProperty.properties.add(name to EnumeratedProperty(booleanArray.map { SimpleProperty(JsonPrimitive(it)) }))
        }

        override fun storeStringArray(stringArray: Array<String>, name: PropertyName) {
            namedProperty.properties.add(name to EnumeratedProperty(stringArray.map { SimpleProperty(JsonPrimitive(it)) }))
        }

        override fun storeULongArray(uLongArray: ULongArray, name: PropertyName) {
            namedProperty.properties.add(name to EnumeratedProperty(uLongArray.map { SimpleProperty(JsonPrimitive(it.toString())) }))
        }

        override fun advance(name: PropertyName): PropertiesBuilder {
            return PropertiesBuilderImpl(case, factory).apply child@{
                this@PropertiesBuilderImpl.namedProperty.properties.add(name to this@child.namedProperty)
            }
        }

        override fun get(): AsyncPropertyLoader {
            return factory.get(namedProperty.toJson().toString()).async()
        }
    }
}