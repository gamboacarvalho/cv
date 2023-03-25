package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object JsonParserCommons {

    private val basicParser: Map<KClass<*>, (String) -> Any> = mapOf(
            Byte::class to { it.toByte() },
            Short::class to { it.toShort() },
            Int::class to { it.toInt() },
            Long::class to { it.toLong() },
            Float::class to { it.toFloat() },
            Double::class to { it.toDouble() },
            Boolean::class to { it.toBoolean() }
    )

    fun <T : Any> parsePrimitive(tokens: JsonTokens, klass: KClass<T>): T? {
        val word = tokens.popWordPrimitive()
        if (!klass.java.isPrimitive || klass.isSubclassOf(String::class))
            return if (word.toLowerCase() == "null") null
            else throw Exception("Looking for a primitive but requires instance of $klass")
        return basicParser[klass]?.invoke(word) as T
    }

    inline fun fillObject(target: Any, setters: Map<String, Setter>, tokens: JsonTokens, klass: KClass<*>) {
        tokens.pop(OBJECT_OPEN) // Discard bracket { OBJECT_OPEN
        while (tokens.current != OBJECT_END) {
            var word = tokens.popWordFinishedWith(COLON)
            word = word.replace("\"", "") // Remove double quotes if present
            val setter = setters[word] ?: throw Exception("$klass does not contain a property with name $word")
            setter.apply(target, tokens)
            if (tokens.current == COMMA) // The last field finishes with bracket } rather than a comma
                tokens.pop(COMMA) // Discard COMMA
            else
                break
        }
        tokens.pop(OBJECT_END) // Discard bracket } OBJECT_END
    }
}
