/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package pt.isel

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParserObjects {

    @Test fun testParseComposePersonReflect() = parsePerson(JsonParserReflectProps)

    @Test fun testParseComposePersonUnsafe() = parsePerson(JsonParserUnsafe)

    @Test fun testParseDateReflect() = parseDate(JsonParserReflectCtor)

    @Test fun testParseDateUnsafe() = parseDate(JsonParserUnsafe)


    private fun parsePerson(parser: JsonParser) {
        val json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}"
        val student = parsePerson(json, parser)
        assertEquals("Ze Manel", student?.name)
        assertEquals(19, student?.birth?.day)
        assertEquals(9, student?.birth?.month)
        assertEquals(1999, student?.birth?.year)
    }

    private fun parseDate(parser: JsonParser) {
        val json = "{ year: 1999, month: 9, day: 19}"
        val dt = parseDate(json, parser)
        assertEquals(19, dt?.day)
        assertEquals(9, dt?.month)
        assertEquals(1999, dt?.year)
    }
}