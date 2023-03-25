package pt.isel

import java.io.DataInputStream
import java.io.InputStream
import java.nio.charset.Charset

class JsonTokensReader(input: InputStream) : JsonTokens{

    private val src = DataInputStream(input)

    override var current: Char = src.read().toChar()
        private set

    override fun tryAdvance(): Boolean {
        current = src.readChar()
        return current.code >= 0
    }

    override fun trim() {
        while (current == ' ' || current == '\n' || current == '\r')
            if (!tryAdvance())
                break
    }

    override fun pop(): Char {
        val token = current
        tryAdvance()
        return token
    }

    override fun pop(expected: Char) {
        if (current != expected)
            throw Exception("Expected $expected but found $current")
        tryAdvance()
    }

    override fun popWordFinishedWith(delimiter: Char): String {
        trim()
        var acc = ""
        while (current != delimiter) {
            acc += current
            tryAdvance()
        }
        tryAdvance() // Discard delimiter
        trim()
        return acc
    }

    override fun popWordPrimitive(): String {
        trim()
        var acc = ""
        while (!isEnd(current)) {
            acc += current
            tryAdvance()
        }
        trim()
        return acc
    }

}
