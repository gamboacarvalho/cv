package pt.isel

class JsonTokensString(json: String) : JsonTokens {
    private val src = json.toCharArray()
    private var index = 0
    override val current: Char
        get() = src[index]

    override fun tryAdvance(): Boolean {
        index++
        return index < src.size
    }

    override fun trim() {
        while (src[index] == ' ')
            if (!tryAdvance())
                break
    }

    override fun pop(): Char {
        val token = src[index]
        index++
        return token
    }

    override fun pop(expected: Char) {
        if (current != expected)
            throw Exception("Expected $expected but found $current")
        index++
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
