package pt.isel

import com.squareup.javapoet.JavaFile
import org.junit.Test
import pt.isel.JsonParserDynamicPoet.buildSetter
import pt.isel.sample.Student
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.test.assertEquals

class SetterDynamicBuildTest {

    @Test fun testSetterBuildOfPersonNr() {
        val source: JavaFile = Student::class
                .declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "nr"}
                .let { buildSetter(Student::class.java, it) }
        val obj = loadAndCreateInstance(source) as Setter
        Student().let {
            obj.apply(it, JsonTokensString("2736, "))
            assertEquals(2736, it.nr)
        }
    }

    @Test fun testSetterBuildOfPersonFields() {
        Student::class
                .declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .map { buildSetter(Student::class.java, it) }
                .forEach { loadAndCreateInstance(it) }
    }
}
