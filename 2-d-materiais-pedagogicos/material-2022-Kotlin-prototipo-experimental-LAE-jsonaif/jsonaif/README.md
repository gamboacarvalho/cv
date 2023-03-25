## Usage

To run these benchmarks on you local machine just run:

```
gradlew jmhJar
```

And then:

```
java -jar jsonaif-bench\build\libs\jsonaif-bench-jmh.jar -i 4 -wi 4 -f 1 -r 2 -w 2 
```

* `-i`  4 iterations
* `-wi` 4 warmup iterations
* `-f`  1 fork
* `-r`  2 run each iteration for 2 seconds
* `-w`  2 run each warmup iteration for 2 seconds.

We are running 3 different workloads for three different kinds of classes:
* `parseDate...` - with fields of primitive types (i.e. `int`)
* `parsePerson...` - with a single `String` field
* `parsePersonAndBirthDate...` - combining two above classes.

And using 4 different introspection approaches:
* `JsonParserReflectCtor` - uses Kotlin Reflection API via class constructor
* `JsonParserReflectProps` - uses Kotlin Reflection API via class properties
* `JsonParserReflectDynamicPoet` - dynamically implementation of `Setter` via JavaPoet
* `JsonParserUnsafe` - manipulating object's memory via `Unsafe`

Sample:
```
Benchmark                            Mode  Cnt     Score     Error   Units
parseDateDynamicPoet                 thrpt    4  1913,236  885,966  ops/ms
parseDateReflectCtor                 thrpt    4    21,560    0,816  ops/ms
parseDateReflectProps                thrpt    4   267,012    6,842  ops/ms
parseDateUnsafe                      thrpt    4  2046,474  292,652  ops/ms
parsePersonAndBirthDateDynamicPoet   thrpt    4   952,264   74,920  ops/ms
parsePersonAndBirthDateReflectCtor   thrpt    4    20,313    2,670  ops/ms
parsePersonAndBirthDateReflectProps  thrpt    4   244,219   16,306  ops/ms
parsePersonAndBirthDateUnsafe        thrpt    4  1215,780   76,976  ops/ms
parsePersonDynamicPoet               thrpt    4  3546,303  107,849  ops/ms
parsePersonReflectCtor               thrpt    4   792,057   70,719  ops/ms
parsePersonReflectProps              thrpt    4  2849,645  301,385  ops/ms
parsePersonUnsafe                    thrpt    4  3575,412  217,139  ops/ms
```