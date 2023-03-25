package pt.isel

import pt.isel.sample.Person
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParseSequenceTest {
    @Test fun testJsonParseJsonArrayToSequence() {
        val json = """[
            {name: "Ze Manel"},
            {name: "Candida Raimunda"},
            {name: "Kata Mandala"}
        ]"""
        val file = "arrayOfPersons.json"
        Files.write(Path.of(file), listOf(json), StandardCharsets.UTF_16LE)
        val ps = JsonParserReflectProps.parseSequence(file, Person::class).iterator()
        assertEquals("Ze Manel", ps.next().let { it as Person }.name)
        assertEquals("Candida Raimunda", ps.next().let { it as Person }.name)
        assertEquals("Kata Mandala", ps.next().let { it as Person }.name)
    }
}
