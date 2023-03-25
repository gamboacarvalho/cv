package pt.isel

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.lang.String.format
import java.lang.reflect.Field
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaSetter


object JsonParserDynamicPoet : AbstractJsonParser() {

    /**
     * For each domain class we keep a Map<String, Setter> relating properties names with their setters.
     */
    private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()

    private fun computeSetters(klass: KClass<*>)  = klass
                .declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .fold(mutableMapOf<String, Setter>()) { acc, curr ->
                    acc[curr.name] = loadAndCreateInstance(buildSetter(klass.java, curr)) as Setter
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
        // val target = Unsafe.UNSAFE.allocateInstance(klass.java)

        val target = klass.java.getDeclaredConstructor()
                ?.newInstance()
                ?: throw Exception("Domain class ${klass.simpleName} is missing a parameterless constructor!")

        val setters = setters.computeIfAbsent(klass, this::computeSetters)
        JsonParserCommons.fillObject(target, setters, tokens, klass)
        return target
    }

    fun buildSetter(klass: Class<*>, prop: KMutableProperty<*>): JavaFile {
        return when (val propKlass = prop.returnType.classifier as KClass<*>) {
            Byte::class -> buildSetter(klass, prop, "byte v = Byte.parseByte(tokens.popWordPrimitive())")
            Short::class -> buildSetter(klass, prop, "short v = Short.parseShort(tokens.popWordPrimitive())")
            Int::class -> buildSetter(klass, prop, "int v = Integer.parseInt(tokens.popWordPrimitive())")
            Long::class -> buildSetter(klass, prop, "long v = Long.parseLong(tokens.popWordPrimitive())")
            Float::class -> buildSetter(klass, prop, "float v = Float.parseFloat(tokens.popWordPrimitive())")
            Double::class -> buildSetter(klass, prop, "double v = Double.parseDouble(tokens.popWordPrimitive())")
            else -> buildSetter(klass, prop, format(
                "%s v = (%s) pt.isel.JsonParserDynamicPoet.INSTANCE.parse(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass(%s.class))",
                propKlass.java.name,
                propKlass.java.name,
                propKlass.java.name ))
        }
    }

    private fun buildSetter(klass: Class<*>, prop: KMutableProperty<*>, stmtParseJson: String): JavaFile {
        val main = MethodSpec.methodBuilder("apply")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)
                .addParameter(Object::class.java, "target")
                .addParameter(JsonTokens::class.java, "tokens")
                .addStatement(stmtParseJson)
                .addStatement("((\$T) target).\$L(v)", klass, prop.javaSetter!!.name)
                .build()

        val setter = TypeSpec
                .classBuilder("Setter${klass.simpleName}_${prop.name}")
                .addSuperinterface(Setter::class.java)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build()

        return JavaFile
                .builder("", setter)
                .build()
    }
}
