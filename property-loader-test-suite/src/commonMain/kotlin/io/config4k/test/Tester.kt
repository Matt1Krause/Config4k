package io.config4k.test

import io.config4k.PropertyName
import io.config4k.PropertyPath
import io.config4k.async.AsyncPropertyLoader
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class Tester(
    private val namingCharPool: List<Char> = defaultNamingCharPool,
    private val stringCharPool: List<Char> = defaultNamingCharPool,
    private val randomTestCaseRepetition: Int = 1000
) {
    companion object {
        private val defaultNamingCharPool: List<Char> = (('a'..'z')).toList()
    }

    private val nameGen = RandomNameGen { random -> Array(random.nextInt(2, 5)) { random.nextString(namingCharPool, 5, 10) } }
    private val tests: MutableList<TypeTest<*>> = ArrayList()

    abstract fun propertyBuilder(): PropertiesBuilder

    private abstract inner class TesterTypeTest<T>(typeName: String) : TypeTest<T>(typeName, nameGen, Random) {
        init {
            @Suppress("LeakingThis")
            tests.add(this)
        }

        override suspend fun propertiesBuilder(): PropertiesBuilder = this@Tester.propertyBuilder()
    }

    /*
    types = ["Byte", "Short", "Int", "Long", "Float", "Double", "UByte", "UShort", "UInt", "ULong"]
    for type in types:
        print("object: TesterTypeTest<" + type + ">(\"" + type + "\") {")
        print("override fun Random.generate(): " + type + " = next" + type + "()")
        print("override suspend fun PropertiesBuilder.store(value: " + type + ", name: PropertyName ) = store" + type + "(value, name)")
        print("override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): " + type + " = load" + type + "(propertyPath, name)")
        print("override suspend fun AsyncPropertyLoader.load(name: PropertyName): " + type + " = load" + type + "(name)")
        print("}.commonTests().numberTests()")
    types = ["ByteArray", "ShortArray", "IntArray", "LongArray", "FloatArray", "DoubleArray", "UByteArray", "UShortArray", "UIntArray", "ULongArray"]
        for type in types:
            print("object: TesterTypeTest<" + type + ">(\"" + type + "\") {")
            print("override fun Random.generate(): " + type + " = generateArray(::" + type + ") {}")
            print("override suspend fun PropertiesBuilder.store(value: " + type + ", name: PropertyName ) = store" + type + "(value, name)")
            print("override suspend fun AsyncPropertyLoader.load(name: PropertyName): " + type + " = load" + type + "(name)")
            print("override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): " + type + " = load" + type + "(propertyPath, name)")
            print("override fun " + type + ".valueEquals(other: Any?): Boolean = customEquals(other, " + type + "::contentEquals)")
            print("override fun " + type + ".valueToString(): String = contentToString()")
            print("}.commonTests().arrayTests()")
     */
    init {
        object : TesterTypeTest<Byte>("Byte") {
            override fun Random.generate(): Byte = nextByte()
            override suspend fun PropertiesBuilder.store(value: Byte, name: PropertyName) = storeByte(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Byte = loadByte(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Byte =
                loadByte(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<Short>("Short") {
            override fun Random.generate(): Short = nextShort()
            override suspend fun PropertiesBuilder.store(value: Short, name: PropertyName) = storeShort(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Short = loadShort(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Short =
                loadShort(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<Int>("Int") {
            override fun Random.generate(): Int = nextInt()
            override suspend fun PropertiesBuilder.store(value: Int, name: PropertyName) = storeInt(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Int = loadInt(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Int =
                loadInt(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<Long>("Long") {
            override fun Random.generate(): Long = nextLong()
            override suspend fun PropertiesBuilder.store(value: Long, name: PropertyName) = storeLong(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Long = loadLong(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Long =
                loadLong(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<Float>("Float") {
            override fun Random.generate(): Float = nextFloat()
            override suspend fun PropertiesBuilder.store(value: Float, name: PropertyName) = storeFloat(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Float = loadFloat(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Float =
                loadFloat(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<Double>("Double") {
            override fun Random.generate(): Double = nextDouble()
            override suspend fun PropertiesBuilder.store(value: Double, name: PropertyName) = storeDouble(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Double = loadDouble(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Double =
                loadDouble(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<UByte>("UByte") {
            override fun Random.generate(): UByte = nextUByte()
            override suspend fun PropertiesBuilder.store(value: UByte, name: PropertyName) = storeUByte(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UByte = loadUByte(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UByte =
                loadUByte(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<UShort>("UShort") {
            override fun Random.generate(): UShort = nextUShort()
            override suspend fun PropertiesBuilder.store(value: UShort, name: PropertyName) = storeUShort(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UShort = loadUShort(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UShort =
                loadUShort(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<UInt>("UInt") {
            override fun Random.generate(): UInt = nextUInt()
            override suspend fun PropertiesBuilder.store(value: UInt, name: PropertyName) = storeUInt(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UInt = loadUInt(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UInt =
                loadUInt(propertyPath, name)
        }.commonTests().numberTests()
        object : TesterTypeTest<ULong>("ULong") {
            override fun Random.generate(): ULong = nextULong()
            override suspend fun PropertiesBuilder.store(value: ULong, name: PropertyName) = storeULong(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): ULong = loadULong(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): ULong =
                loadULong(propertyPath, name)
        }.commonTests().numberTests()

        object : TesterTypeTest<Boolean>("Boolean") {
            override fun Random.generate(): Boolean = nextBoolean()
            override suspend fun PropertiesBuilder.store(value: Boolean, name: PropertyName) = storeBoolean(value, name)
            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Boolean = loadBoolean(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): Boolean =
                loadBoolean(propertyPath, name)
        }.commonTests().apply {
            this += FailOnStringLoad
        }

        @Suppress("LeakingThis")
        StringTypeTest(stringCharPool, nameGen, Random, 5, 30, this::propertyBuilder)
            .commonTests()
            .also {
                it += StringStressTest(stringCharPool)
                tests.add(it)
            }

        object : TesterTypeTest<ByteArray>("ByteArray") {
            override fun Random.generate(): ByteArray = generateArray(::ByteArray) { nextByte() }
            override suspend fun PropertiesBuilder.store(value: ByteArray, name: PropertyName) =
                storeByteArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): ByteArray = loadByteArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): ByteArray =
                loadByteArray(propertyPath, name)

            override fun ByteArray.valueEquals(other: Any?): Boolean = customEquals(other, ByteArray::contentEquals)
            override fun ByteArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<ShortArray>("ShortArray") {
            override fun Random.generate(): ShortArray = generateArray(::ShortArray) { nextShort() }
            override suspend fun PropertiesBuilder.store(value: ShortArray, name: PropertyName) =
                storeShortArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): ShortArray = loadShortArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): ShortArray =
                loadShortArray(propertyPath, name)

            override fun ShortArray.valueEquals(other: Any?): Boolean = customEquals(other, ShortArray::contentEquals)
            override fun ShortArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<IntArray>("IntArray") {
            override fun Random.generate(): IntArray = generateArray(::IntArray) { nextInt() }
            override suspend fun PropertiesBuilder.store(value: IntArray, name: PropertyName) =
                storeIntArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): IntArray = loadIntArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): IntArray =
                loadIntArray(propertyPath, name)

            override fun IntArray.valueEquals(other: Any?): Boolean = customEquals(other, IntArray::contentEquals)
            override fun IntArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<LongArray>("LongArray") {
            override fun Random.generate(): LongArray = generateArray(::LongArray) { nextLong() }
            override suspend fun PropertiesBuilder.store(value: LongArray, name: PropertyName) =
                storeLongArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): LongArray = loadLongArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): LongArray =
                loadLongArray(propertyPath, name)

            override fun LongArray.valueEquals(other: Any?): Boolean = customEquals(other, LongArray::contentEquals)
            override fun LongArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<FloatArray>("FloatArray") {
            override fun Random.generate(): FloatArray = generateArray(::FloatArray) { nextFloat() }
            override suspend fun PropertiesBuilder.store(value: FloatArray, name: PropertyName) =
                storeFloatArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): FloatArray = loadFloatArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): FloatArray =
                loadFloatArray(propertyPath, name)

            override fun FloatArray.valueEquals(other: Any?): Boolean = customEquals(other, FloatArray::contentEquals)
            override fun FloatArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<DoubleArray>("DoubleArray") {
            override fun Random.generate(): DoubleArray = generateArray(::DoubleArray) { nextDouble() }
            override suspend fun PropertiesBuilder.store(value: DoubleArray, name: PropertyName) =
                storeDoubleArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): DoubleArray = loadDoubleArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): DoubleArray =
                loadDoubleArray(propertyPath, name)

            override fun DoubleArray.valueEquals(other: Any?): Boolean = customEquals(other, DoubleArray::contentEquals)
            override fun DoubleArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<UByteArray>("UByteArray") {
            override fun Random.generate(): UByteArray = generateArray(::UByteArray) { nextUByte() }
            override suspend fun PropertiesBuilder.store(value: UByteArray, name: PropertyName) =
                storeUByteArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UByteArray = loadUByteArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UByteArray =
                loadUByteArray(propertyPath, name)

            override fun UByteArray.valueEquals(other: Any?): Boolean = customEquals(other, UByteArray::contentEquals)
            override fun UByteArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<UShortArray>("UShortArray") {
            override fun Random.generate(): UShortArray = generateArray(::UShortArray) { nextUShort() }
            override suspend fun PropertiesBuilder.store(value: UShortArray, name: PropertyName) =
                storeUShortArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UShortArray = loadUShortArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UShortArray =
                loadUShortArray(propertyPath, name)

            override fun UShortArray.valueEquals(other: Any?): Boolean = customEquals(other, UShortArray::contentEquals)
            override fun UShortArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<UIntArray>("UIntArray") {
            override fun Random.generate(): UIntArray = generateArray(::UIntArray) { nextUInt() }
            override suspend fun PropertiesBuilder.store(value: UIntArray, name: PropertyName) =
                storeUIntArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): UIntArray = loadUIntArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): UIntArray =
                loadUIntArray(propertyPath, name)

            override fun UIntArray.valueEquals(other: Any?): Boolean = customEquals(other, UIntArray::contentEquals)
            override fun UIntArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()
        object : TesterTypeTest<ULongArray>("ULongArray") {
            override fun Random.generate(): ULongArray = generateArray(::ULongArray) { nextULong() }
            override suspend fun PropertiesBuilder.store(value: ULongArray, name: PropertyName) =
                storeULongArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): ULongArray = loadULongArray(name)
            override suspend fun AsyncPropertyLoader.load(propertyPath: PropertyPath, name: PropertyName): ULongArray =
                loadULongArray(propertyPath, name)

            override fun ULongArray.valueEquals(other: Any?): Boolean = customEquals(other, ULongArray::contentEquals)
            override fun ULongArray.valueToString(): String = contentToString()
        }.commonTests().numberArrayTests()

        object : TesterTypeTest<BooleanArray>("BooleanArray") {
            override fun Random.generate(): BooleanArray = generateArray(::BooleanArray) { nextBoolean() }
            override suspend fun PropertiesBuilder.store(value: BooleanArray, name: PropertyName) =
                storeBooleanArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): BooleanArray = loadBooleanArray(name)
            override suspend fun AsyncPropertyLoader.load(
                propertyPath: PropertyPath,
                name: PropertyName
            ): BooleanArray = loadBooleanArray(propertyPath, name)

            override fun BooleanArray.valueEquals(other: Any?): Boolean =
                customEquals(other, BooleanArray::contentEquals)

            override fun BooleanArray.valueToString(): String = contentToString()
        }.commonTests().arrayTests().apply {
            this += FailOnStringArrayLoad
        }

        object : TesterTypeTest<Array<String>>("StringArray") {
            override fun Random.generate(): Array<String> = generateArray(::Array) { nextString() }
            override suspend fun PropertiesBuilder.store(value: Array<String>, name: PropertyName) =
                storeStringArray(value, name)

            override suspend fun AsyncPropertyLoader.load(name: PropertyName): Array<String> = loadStringArray(name)
            override suspend fun AsyncPropertyLoader.load(
                propertyPath: PropertyPath,
                name: PropertyName
            ): Array<String> = loadStringArray(propertyPath, name)

            override fun Array<String>.valueEquals(other: Any?): Boolean =
                customEquals(other, Array<String>::contentDeepEquals)

            override fun Array<String>.valueToString(): String = contentDeepToString()
        }

        @Suppress("LeakingThis")
        StringArrayTypeTest(stringCharPool, nameGen, Random, 5, 30, 5, 10, this::propertyBuilder)
            .commonTests()
            .arrayTests()
            .also {
                tests.add(it)
            }
    }

    suspend fun execute() {
        for (test in tests) {
            test.execute()
        }
        repeat(randomTestCaseRepetition) {
            val rd = RandomTestData(Random(Random.nextLong()), tests, nameGen)
            val pb = propertyBuilder()
            rd.store(pb)
            rd.load(pb.get(), emptyList())
        }
    }
}