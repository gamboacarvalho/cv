package pt.isel

import kotlin.reflect.KClass

interface JsonParser {

    fun <T : Any> parse(tokens: JsonTokens, klass: KClass<T>) : T?

    fun <T : Any> parseArray(tokens: JsonTokens, klass: KClass<T>): List<T?>
}

inline fun <reified T : Any> JsonParser.parse(source: String): T? {
    return parse(JsonTokensString(source), T::class)
}

inline fun <reified T : Any> JsonParser.parseArray(source: String): List<T?> {
    return parseArray(JsonTokensString(source), T::class)
}
