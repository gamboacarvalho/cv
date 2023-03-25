package pt.isel

import java.io.FileReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass


abstract class AbstractJsonParser : JsonParser {

    override fun <T : Any> parse(tokens: JsonTokens, klass: KClass<T>): T? {
        return when (tokens.current) {
            OBJECT_OPEN -> parseObject(tokens, klass)
            DOUBLE_QUOTES -> parseString(tokens)
            else -> parsePrimitive(tokens, klass)
        }
    }

    abstract fun <T : Any> parsePrimitive(tokens: JsonTokens, klass: KClass<T>): T?

    abstract fun <T : Any> parseObject(tokens: JsonTokens, klass: KClass<T>): T?

    private fun <T> parseString(tokens: JsonTokens): T {
        tokens.pop(DOUBLE_QUOTES) // Discard double quotes "
        return tokens.popWordFinishedWith(DOUBLE_QUOTES) as T
    }

    override fun <T : Any> parseArray(tokens: JsonTokens, klass: KClass<T>): List<T?> {
        val list = mutableListOf<T?>()
        tokens.pop(ARRAY_OPEN) // Discard square brackets [ ARRAY_OPEN
        while (tokens.current != ARRAY_END) {
            val v = parse(tokens, klass)
            list.add(v)
            if (tokens.current == COMMA) // The last element finishes with ] rather than a comma
                tokens.pop(COMMA) // Discard COMMA
            else break
            tokens.trim()
        }
        tokens.pop(ARRAY_END) // Discard square bracket ] ARRAY_END
        return list;
    }

    fun parseSequence(path: String, klass: KClass<*>): Sequence<Any?> {
        return sequence {
            (Files.newInputStream(Path.of(path))).use {
                val tokens = JsonTokensReader(it)
                tokens.pop(ARRAY_OPEN) // Discard square brackets [ ARRAY_OPEN
                while (tokens.current != ARRAY_END) {
                    tokens.trim()
                    val v = parse(tokens, klass)
                    yield(v)
                    if (tokens.current == COMMA) // The last element finishes with ] rather than a comma
                        tokens.pop(COMMA) // Discard COMMA
                    else break
                }
                tokens.pop(ARRAY_END) // Discard square bracket ] ARRAY_END
            }
        }
    }
}
