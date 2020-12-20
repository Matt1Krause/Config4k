package test

import io.config4k.annotation.Properties
import io.config4k.annotation.PropertiesConstructor
import io.config4k.annotation.PropertyName
import io.config4k.json.JsonPropertyLoader
import io.config4k.jvm.from
import java.io.File

@Properties
data class TestClass(
     val theAnswer: Int,
    val text: String,
    val values: IntArray,
    val textValues: Array<String>,
    val subclass: Subclass
)

@Properties
data class Subclass(
    val bool: Boolean
)

@Properties
internal data class NaturalInternal(@PropertyName("this", "is", "a", "name") val thisIsNotAName: String)
@Properties(true)
data class ForcedInternalClass(val bestShow: String, val bestDoctor: String) {
    @PropertiesConstructor
    constructor(bestDoctor: String): this("Doctor Who", bestDoctor)
}

fun main() {
    //scanPath(Path.of(""))
    val loader = JsonPropertyLoader from File("plugin-test\\build\\resources\\main\\properties.json")
    try {
        println(TestClass(loader))
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    println(NaturalInternal(loader))
    println(ForcedInternalClass(loader))
}