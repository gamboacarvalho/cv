package pt.isel

import pt.isel.JsonParserCommons.fillObject
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


object JsonParserUnsafe : AbstractJsonParser() {

    /**
     * For each domain class we keep a Map<String, Setter> relating field names with their offset.
     */
    private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()

    private fun computeOffsets(klass: KClass<*>) = klass
            .memberProperties
            .fold(mutableMapOf<String, Setter>()) { acc, curr ->
                val offset = Unsafe.UNSAFE.objectFieldOffset(curr.javaField)
                val propKlass = curr.returnType.classifier as KClass<*>
                acc[curr.name] = buildSetter(offset, propKlass)
                acc
            }

    /**
     * parsePrimitive() is boxing intrinsically due to return type being Any.
     * This is harmless to the Reflection API since it deals with Object or Any.
     * Yet, for unsafe operations mapping memory directly we do not need to box primitive values.
     */
    override fun <T : Any> parsePrimitive(tokens: JsonTokens, klass: KClass<T>): T? {
        throw UnsupportedOperationException("Not implemented in Unsafe approach to avoid boxing and unboxing!")
    }

    override fun <T : Any> parseObject(tokens: JsonTokens, klass: KClass<T>): T? {
        val target = Unsafe.UNSAFE.allocateInstance(klass.java)
        val offsets = setters.computeIfAbsent(klass, this::computeOffsets)
        fillObject(target, offsets, tokens, klass)
        return target as T
    }

    /**
     * For unsafe operations mapping memory directly we do not need to box primitive values.
     * With this approach we are avoiding to box and unbox of primitive values.
     * If this is not optimized enough then do it in Java.
     * But, from what I can see through javap there are no boxing ops in next operations.
     */
    private fun buildSetter(offset: Long, fieldKlass: KClass<*>): Setter = when (fieldKlass) {
        Byte::class -> SetterByte(offset)
        Short::class -> SetterShort(offset)
        Int::class -> SetterInt(offset)
        Long::class -> SetterLong(offset)
        Float::class -> SetterFloat(offset)
        Double::class -> SetterDouble(offset)
        Boolean::class -> SetterBoolean(offset)
        else -> SetterObject(offset, fieldKlass)
    }

    class SetterByte(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Byte = tokens.popWordPrimitive().toByte()
            Unsafe.UNSAFE.putByte(target, offset, v)
        }
    }

    class SetterShort(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Short = tokens.popWordPrimitive().toShort()
            Unsafe.UNSAFE.putShort(target, offset, v)
        }
    }

    class SetterInt(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Int = tokens.popWordPrimitive().toInt()
            Unsafe.UNSAFE.putInt(target, offset, v)
        }
    }

    class SetterLong(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Long = tokens.popWordPrimitive().toLong()
            Unsafe.UNSAFE.putLong(target, offset, v)
        }
    }

    class SetterFloat(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Float = tokens.popWordPrimitive().toFloat()
            Unsafe.UNSAFE.putFloat(target, offset, v)
        }
    }

    class SetterDouble(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Double = tokens.popWordPrimitive().toDouble()
            Unsafe.UNSAFE.putDouble(target, offset, v)
        }
    }

    class SetterBoolean(private val offset: Long) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Boolean = tokens.popWordPrimitive().toBoolean()
            Unsafe.UNSAFE.putBoolean(target, offset, v)
        }
    }

    class SetterObject(private val offset: Long, private val fieldKlass: KClass<*>) : Setter {
        override fun apply(target: Any, tokens: JsonTokens) {
            val v: Any? = JsonParserUnsafe.parse(tokens, fieldKlass)
            Unsafe.UNSAFE.putObject(target, offset, v)
        }
    }
}
