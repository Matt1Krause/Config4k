package io.config4k

typealias PropertyName = Array<String>

interface PropertyPath {
    val names: List<PropertyName>
    fun preComputedNamesFor(case: NameCase): Iterator<String>
    val length get() = names.size
}

class ConcatenatedPropertyPath(private val pp1: PropertyPath, private val pp2: PropertyPath) : PropertyPath {
    override val names: List<PropertyName> = pp1.names + pp2.names
    override fun preComputedNamesFor(case: NameCase): Iterator<String> =
        pp1.preComputedNamesFor(case) + pp2.preComputedNamesFor(case)

    private class ConcatIterator(private val i1: Iterator<String>, private val i2: Iterator<String>) :
        Iterator<String> {
        override fun hasNext(): Boolean = i1.hasNext() || i2.hasNext()
        override fun next(): String = if (i1.hasNext()) i1.next() else i2.next()
    }

    private inline operator fun Iterator<String>.plus(other: Iterator<String>): Iterator<String> =
        ConcatIterator(this, other)
}

operator fun PropertyPath.plus(path: PropertyPath): PropertyPath = ConcatenatedPropertyPath(this, path)

object EmptyPropertyPath: PropertyPath {
    override val names: List<PropertyName> = emptyList()
    private val iter = names.iterator()
    @Suppress("UNCHECKED_CAST")
    override fun preComputedNamesFor(case: NameCase): Iterator<String> = iter as Iterator<String>
}

class SingleElementPropertyPath(name: PropertyName): PropertyPath {
    override val names: List<PropertyName> = listOf(name)
    private val nameCaseContainer = NameCaseContainer(name)
    override fun preComputedNamesFor(case: NameCase): Iterator<String> = SingleElementIterator(case)

    private inner class SingleElementIterator(private val nameCase: NameCase): Iterator<String> {
        private var requested: Boolean = false
        override fun hasNext(): Boolean = !requested
        override fun next(): String = if (!requested) {
            requested = true
            nameCaseContainer.computeNameFor(nameCase)
        } else {
            throw IllegalStateException("request more elements from iterator, even though hasNext = false")
        }
    }
}

class MultiElementPropertyPath(override val names: List<PropertyName>, private val preComputedNames: List<NameCaseContainer>):
    PropertyPath {
    override fun preComputedNamesFor(case: NameCase): Iterator<String> = PreComputedNamesIterator(case)

    private inner class PreComputedNamesIterator(private val case: NameCase): Iterator<String> {
        private val delegate = preComputedNames.iterator()

        override fun hasNext(): Boolean = delegate.hasNext()
        override fun next(): String = delegate.next().computeNameFor(case)
    }
}