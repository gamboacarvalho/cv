package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.primaryConstructor


object JsonParserReflectCtor : AbstractJsonParser() {

    override fun <T : Any> parsePrimitive(tokens: JsonTokens, klass: KClass<T>) =  JsonParserCommons.parsePrimitive(tokens, klass)

    override fun <T : Any> parseObject(tokens: JsonTokens, klass: KClass<T>) : T? {
        val ctor = klass.primaryConstructor ?: throw Exception("Missing a primary constructor on $klass!!!")
        val args = mutableMapOf<String, Any?>()
        tokens.pop(OBJECT_OPEN) // Discard bracket { OBJECT_OPEN
        while (tokens.current != OBJECT_END) {
            var word = tokens.popWordFinishedWith(COLON)
            word = word.replace("\"", "") // Remove double quotes if present
            val arg = ctor
                    .findParameterByName(word)
                    ?: throw Exception("$klass does not contain a property with name $word")
            val v = parse(tokens, arg.type.classifier as KClass<*>)
            args[word] = v
            if (tokens.current == COMMA) // The last field finishes with bracket } rather than a comma
                tokens.pop(COMMA) // Discard COMMA
            else
                break
        }
        tokens.pop(OBJECT_END) // Discard bracket } OBJECT_END
        /*
         * Sort the constructor's arguments values by the same order of its parameters
         * and finally create an object invoking that constructor.
         */
        return ctor
                .parameters
                .map { args[it.name] }
                .let { ctor.call(*it.toTypedArray()) }
    }
}
