# Buffer

- The buffer() operator allows emission and collection to run concurrently (in separate coroutines internally).

- By default flow is sequential that means emit is followed by collection sequentially.

- Using buffer we can emit items independently to collect operation, (both happens in separate coroutines internally)

- useful when emitter is fast but collection is slow and similar cases

- flow does not block the emission because of collection, when using buffer

```
fun main() {
  val time = measureTimeMillis {
      runBlocking {
          flowSource().buffer().collect {
              delay(1000)
             println("Collecting values $it")
          }
      }
  }
  println("Total time : $time")
}

fun flowSource(): Flow<Int> = flow {
    for (i in 1..5) {
        emit(i)
    }
}

EMITTER --> [Buffer] ---> COLLECTOR
```

# Conflation

- When we dont want to process all values, like incase collector is slow then we can use conflate to collect the last latest value. 

- Conflate is like a buffer with capacity 1.

```
fun main() {
    runBlocking {
        source().conflate().collect{
            delay(1000)
            println("Collecting value: $it")
        }
    }
}

fun source(): Flow<Int> = flow {
    for(i in 1..10) {
        emit(i)
    }
}
```