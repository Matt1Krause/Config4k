package io.config4k.plugin.schema

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName

interface Scheme {
    val propertiesClass: IrClass
    val propertiesFactory: IrClass
    val loadBlocking: IrFunction?
    val loadAsync: IrFunction?
    val fullName: FqName
}

data class ExternalScheme(
    override val propertiesClass: IrClass,
    override val propertiesFactory: IrClass,
    override val loadBlocking: IrFunction?,
    override val loadAsync: IrFunction?
) : Scheme {
    override val fullName: FqName = propertiesClass.kotlinFqName
}

class SourceScheme(
    override val propertiesClass: IrClass,
    override val propertiesFactory: IrClass,
    val propertiesConstructor: IrConstructor,
    override val loadBlocking: IrFunction?,
    override val loadAsync: IrFunction?,
    val companionLoadBlocking: IrFunction?,
    val companionLoadAsync: IrFunction?,
    val cascade: Boolean
) : Scheme {
    override val fullName: FqName = propertiesClass.kotlinFqName

    private val backingProperties: MutableList<Property> = ArrayList()
    val properties: List<Property> = backingProperties
    private val backingDependsOn: MutableList<Scheme> = ArrayList()

    fun addProperty(property: Property) {
        backingProperties.add(property)
    }
    inline operator fun plusAssign(property: Property) = addProperty(property)
}

inline fun SourceScheme.addProperty(fieldName: String, propertyName: List<String>, index: Int, type: PropertyType, inlined: Boolean) =
    addProperty(Property(fieldName, propertyName, index, type, inlined))