# Flow

- Flow methods are not marked with suspend
- Doesnt block the thread they run on.
- Refer e.g below

```
fun main() {
  runBlocking {
      launch {
         println("I am running on thread .. ${Thread.currentThread().name}")
          for (k in 1..10) {
              delay(1000)
              println("I am not blocked.. ${k}")
          }
      }

      emitItems().collect{
          println("Collecting from Flow... ${it}")
      }

  }
}

fun emitItems(): Flow<Int> = flow {
    for (k in 1..10) {
        delay(1000)
        emit(k)
    }
}

Output: 

I am running on thread .. main
Collecting from Flow... 1
I am not blocked.. 1
Collecting from Flow... 2
I am not blocked.. 2
Collecting from Flow... 3
I am not blocked.. 3
Collecting from Flow... 4
I am not blocked.. 4
Collecting from Flow... 5
I am not blocked.. 5
Collecting from Flow... 6
I am not blocked.. 6
Collecting from Flow... 7
I am not blocked.. 7
Collecting from Flow... 8
I am not blocked.. 8
Collecting from Flow... 9
I am not blocked.. 9
Collecting from Flow... 10
I am not blocked.. 10

```

## Flow are cold

- Flows are cold streams similar to sequences — the code inside a flow builder does not run until the flow is collected. 

- Flow starts fresh with every collect. 

```
fun simple(): Flow<Int> = flow { 
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    println("Calling simple function...")
    val flow = simple()
    println("Calling collect...")
    flow.collect { value -> println(value) } 
    println("Calling collect again...")
    flow.collect { value -> println(value) } 
}

Output

Calling simple function...
Calling collect...
Flow started
1
2
3
Calling collect again...
Flow started
1
2
3
```

# Flow cancellation basics

- Flows adhere to the general cooperative cancellation of coroutines. As usual, flow collection can be cancelled when the flow is suspended in a cancellable suspending function (like delay). Delay will check for cancellation internally. 

```
fun main() {
  runBlocking {
      withTimeoutOrNull(2000) {
          simpleFlow().collect{
              println("Collecting from flow $it")
          }
      }
  }
}

fun simpleFlow() : Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
        println("Emitting value $i")
        emit(i)
    }
}
```

# Flow builders

- flow {}
- asFlow
- flowOf

```
fun main() {
    runBlocking {
        (1..3).asFlow().collect{
         println("Printing emitted values $it")
        }

        flowOf(1,2,3,4,5).collect{
            println("Printing emitted values from Flow Of $it")
        }
    }

}

```

# Flow intermediate operations

- Operators like map, filter etc are similar to that of sequences, but also allow suspend functions to be called from within them

```
fun main() {
 runBlocking {
     (1..5).asFlow().map { request -> perform(request) }.collect{
         println("Logs $it")
     }
 }
}

suspend fun perform(request: Int): String {
    delay(1000)
    return "Request: $request Response: ${2*request}"
}

```

## Flow transform operation

- Can be used to achieve operation like map and filter.
- Also can emit different kind of values in the same stream like below: it emits string before a long running task

```
fun main() {
    runBlocking {
        (1..5).asFlow().transform {request ->
            emit("Requested $request ")
            emit(performRequest(request))
        }.collect{
            println("Response : $it")
        }
    }
}


suspend fun performRequest(request: Int): String {
    delay(5000)
    return "Request: $request Response: ${2*request}"
}

```

## Flow size limit 

- take(n) operation
- collects first n

```
 runBlocking {
        (1..10).asFlow().take(2).collect{
            println("Collecting from Flow ${it}")
        }
    }
```

## Terminal Operators

- Terminal operators on flows are suspending functions that start a collection of the flow. The collect operator is the most basic one, but there are other terminal operators, which can make it easier

- Below eg. Using reduce

```
/**
 * 1 + 4 + 9 + 16 + 25 = 55
 */
fun main() {
    runBlocking {
        val sum = (1..5).asFlow().map {
             it*it
        }.reduce { a, b ->  a + b}

        println("Sum is $sum")
    }
}
```

# Flow context - Important Concept

- Collection of a flow always happens in the context of the calling coroutine. For example, if there is a simple flow, then the following code runs in the context specified by the author of this code, regardless of the implementation details of the simple flow:

This property of a flow is called context preservation.



```
withContext(context) {
    simple().collect { value ->
        println(value) // run in the specified context
    }
}

```

- So, by default, code in the flow { ... } builder runs in the context that is provided by a collector of the corresponding flow. For example, consider the implementation of a simple function that prints the thread it is called on and emits three numbers:

```
fun simple(): Flow<Int> = flow {
    log("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}  

fun main() = runBlocking<Unit> {
    simple().collect { value -> log("Collected $value") } 
}            
```

## Common pitfall of Context

- Wrong way of changing context of Flow

```
fun main() {
    runBlocking {
        simple().collect{
            println("Collecting from simple $it")
        }
    }
}

fun simple(): Flow<Int> = flow {
    withContext(Dispatchers.Default) { //Dont do this
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }
}
```

- Correct way: use flowOn

```
fun main() {
    runBlocking {
        simple().collect{
            println("Collecting from simple $it")
        }
    }
}

fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
}.flowOn(Dispatchers.Default)
```