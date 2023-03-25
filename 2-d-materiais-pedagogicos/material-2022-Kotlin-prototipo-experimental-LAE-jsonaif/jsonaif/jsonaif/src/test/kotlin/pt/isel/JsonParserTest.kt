package pt.isel

import pt.isel.sample.Person
import pt.isel.sample.Student
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParserTest {
    @Test fun testParseSimpleObjectViaCtor() = parseSimpleObject(JsonParserReflectCtor)

    @Test fun testParseSimpleObjectViaProps() = parseSimpleObject(JsonParserReflectProps)

    @Test fun testParseSimpleObjectUnsafe() = parseSimpleObject(JsonParserUnsafe)

    @Test fun testParseSimpleObjectDynamicPoet() = parseSimpleObject(JsonParserDynamicPoet)

    @Test fun testParseComposeObjectViaCtor() = parseComposeObject(JsonParserReflectCtor)

    @Test fun testParseComposeObjectViaProps() = parseComposeObject(JsonParserReflectProps)

    @Test fun testParseComposeObjectUnsafe() = parseComposeObject(JsonParserUnsafe)

    @Test fun testParseComposeObjectDynamicPoet() = parseComposeObject(JsonParserDynamicPoet)

    @Test fun testParseArrayViaCtor() = parseArray(JsonParserReflectCtor)

    @Test fun testParseArrayViaProps() = parseArray(JsonParserReflectProps)

    @Test fun testParseArrayUnsafe() = parseArray(JsonParserUnsafe)

    @Test fun testParseArrayDynamicPoet() = parseArray(JsonParserDynamicPoet)

    private fun parseSimpleObject(parser: JsonParser) {
        val json = "{ name: \"Ze Manel\", nr: 7353}"
        val student = parser.parse<Student>(json)
        assertEquals("Ze Manel", student?.name)
        assertEquals(7353, student?.nr)
    }

    private fun parseComposeObject(parser: JsonParser) {
        val json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}, sibling: { name: \"Kata Badala\"}}"
        val student: Person? = parser.parse(json)
        assertEquals("Ze Manel", student?.name)
        assertEquals(19, student?.birth?.day)
        assertEquals(9, student?.birth?.month)
        assertEquals(1999, student?.birth?.year)
    }

    private fun parseArray(parser: JsonParser) {
        val json = "[{name: \"Ze Manel\"}, {name: \"Candida Raimunda\"}, {name: \"Kata Mandala\"}]";
        val ps = parser.parseArray<Person>(json)
        assertEquals(3, ps.size)
        assertEquals("Ze Manel", ps[0]?.name)
        assertEquals("Candida Raimunda", ps[1]?.name)
        assertEquals("Kata Mandala", ps[2]?.name)
    }
}
