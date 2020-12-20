package io.config4k.json

class PropertyTree(private val mapSupplier: MapSupplier = MapSupplier.HashMap, var value: String?){
    companion object {}
    private val children: MutableMap<String, PropertyTree> = mapSupplier.get()

    operator fun set(property: String, node: PropertyTree) {
        if (children.containsKey(property))
            throw IllegalStateException("property $property already exits in this PropertyTree")
        children[property] = node
    }

    operator fun set(property: String, value: String?): Unit = set(property, PropertyTree(mapSupplier, value))

    fun getOrCreate(property: String): PropertyTree {
        return if (children.containsKey(property))
            children[property]!!
        else {
            val newProperty = PropertyTree(mapSupplier, null)
            children[property] = newProperty
            newProperty
        }
    }

    operator fun get(property: String): PropertyTree? {
        return children[property]
    }

    operator fun contains(property: String): Boolean {
        return children.contains(property)
    }
}