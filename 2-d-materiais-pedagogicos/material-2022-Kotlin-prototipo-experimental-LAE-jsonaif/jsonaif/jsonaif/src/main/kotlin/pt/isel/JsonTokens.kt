package pt.isel

const val OBJECT_OPEN = '{'
const val OBJECT_END = '}'
const val ARRAY_OPEN = '['
const val ARRAY_END = ']'
const val DOUBLE_QUOTES = '"'
const val COMMA = ','
const val COLON = ':'

interface JsonTokens {
    val current: Char

    fun tryAdvance(): Boolean

    fun trim()

    fun pop(): Char

    fun pop(expected: Char)

    fun popWordFinishedWith(delimiter: Char): String

    fun popWordPrimitive(): String

    fun isEnd(curr: Char): Boolean {
        return curr == OBJECT_END || curr == ARRAY_END || curr == COMMA
    }
}
