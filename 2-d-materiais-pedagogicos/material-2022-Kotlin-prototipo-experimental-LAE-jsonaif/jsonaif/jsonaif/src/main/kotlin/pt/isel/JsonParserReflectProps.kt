package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties


object JsonParserReflectProps  : AbstractJsonParser() {

    /**
     * For each domain class we keep a Map<String, Setter> relating properties names with their setters.
     */
    private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()

    private fun computeProperties(klass: KClass<*>): MutableMap<String, Setter> = klass
                .memberProperties
                .filterIsInstance<KMutableProperty<*>>() // check if is RW property, i.e. var
                .fold(mutableMapOf()){ acc, curr ->
                    acc[curr.name] = object : Setter {
                        override fun apply(target: Any, tokens: JsonTokens) {
                            curr.setter.call(target, parse(tokens, curr.returnType.classifier as KClass<*>))
                        }
                    }
                    acc
                }

    override fun <T : Any> parsePrimitive(tokens: JsonTokens, klass: KClass<T>) =  JsonParserCommons.parsePrimitive(tokens, klass)

    override fun <T : Any> parseObject(tokens: JsonTokens, klass: KClass<T>) : T? {
        /**
         * Kotlin KClass createInstance() degrades the benchmark in almost 2x.
         */
        // val target = klass.createInstance()
        /**
         * Faster instantiation than the former.
         * Curiously it is so performant as UNSAFE.allocateInstance().
         * Using the  newInstance() reflect call in JsonParserUnsafe does not degrade its performance.
         */
        val target = klass.java.getDeclaredConstructor()
                ?.newInstance()
                ?: throw Exception("Domain class ${klass.simpleName} is missing a parameterless constructor!")
        val props = setters.computeIfAbsent(klass, ::computeProperties)
        JsonParserCommons.fillObject(target, props, tokens, klass)
        return target;
    }
}
