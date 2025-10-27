# Async Usage

- Can run multiple suspend in parallel, and hence can save time. for eg

```
fun main() {
  val time = runBlocking {
      measureTimeMillis {
          val output1 = async { remoteCall1() }
          val output2 = async{ remoteCall2() }
          println("Total Output: ${output1.await() + output2.await()}")
      }
  }
    println("Total time: ${time}")
}

suspend fun remoteCall1(): Int {
    delay(1000)
    return 20
}

suspend fun remoteCall2(): Int {
    delay(2000)
    return 40
}
```

- without async this would take around 1000 + 2000 ms
- with async it takes around 2000 ms. 

## Async Lazy start

- Async can be started lazily like below

```
val time = measureTimeMillis {
    val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
    val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
    // some computation
    one.start() // start the first one
    two.start() // start the second one
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```