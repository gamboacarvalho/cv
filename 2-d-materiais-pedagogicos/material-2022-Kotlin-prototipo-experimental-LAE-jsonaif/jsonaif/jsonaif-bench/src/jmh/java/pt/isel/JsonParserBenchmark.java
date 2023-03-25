package pt.isel;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonParserBenchmark {

    @Benchmark
    public Person parsePersonReflectCtor() {
        final String json = "{ name: \"Ze Manel\" }";
        return JsonParserObjectsKt.parsePerson(json, JsonParserReflectCtor.INSTANCE);
    }

    @Benchmark
    public Person parsePersonReflectProps() {
        final String json = "{ name: \"Ze Manel\" }";
        return JsonParserObjectsKt.parsePerson(json, JsonParserReflectProps.INSTANCE);
    }

    @Benchmark
    public Person parsePersonUnsafe() {
        final String json = "{ name: \"Ze Manel\" }";
        return JsonParserObjectsKt.parsePerson(json, JsonParserUnsafe.INSTANCE);
    }

    @Benchmark
    public Person parsePersonDynamicPoet() {
        final String json = "{ name: \"Ze Manel\" }";
        return JsonParserObjectsKt.parsePerson(json, JsonParserDynamicPoet.INSTANCE);
    }

    @Benchmark
    public Person parsePersonAndBirthDateReflectCtor() {
        final String json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";
        return JsonParserObjectsKt.parsePerson(json, JsonParserReflectCtor.INSTANCE);
    }

    @Benchmark
    public Person parsePersonAndBirthDateReflectProps() {
        final String json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";
        return JsonParserObjectsKt.parsePerson(json, JsonParserReflectProps.INSTANCE);
    }

    @Benchmark
    public Person parsePersonAndBirthDateUnsafe() {
        final String json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";
        return JsonParserObjectsKt.parsePerson(json, JsonParserUnsafe.INSTANCE);
    }

    @Benchmark
    public Person parsePersonAndBirthDateDynamicPoet() {
        final String json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";
        return JsonParserObjectsKt.parsePerson(json, JsonParserDynamicPoet.INSTANCE);
    }

    @Benchmark
    public Date parseDateReflectCtor() {
        final String json = "{ year: 2047, month: 11, day: 23}";
        return JsonParserObjectsKt.parseDate(json, JsonParserReflectCtor.INSTANCE);
    }

    @Benchmark
    public Date parseDateReflectProps() {
        final String json = "{ year: 2047, month: 11, day: 23}";
        return JsonParserObjectsKt.parseDate(json, JsonParserReflectProps.INSTANCE);
    }

    @Benchmark
    public Date parseDateUnsafe() {
        final String json = "{ year: 2047, month: 11, day: 23}";
        return JsonParserObjectsKt.parseDate(json, JsonParserUnsafe.INSTANCE);
    }

    @Benchmark
    public Date parseDateDynamicPoet() {
        final String json = "{ year: 2047, month: 11, day: 23}";
        return JsonParserObjectsKt.parseDate(json, JsonParserDynamicPoet.INSTANCE);
    }
}
