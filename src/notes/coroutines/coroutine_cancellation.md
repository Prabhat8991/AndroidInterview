### Cancellation

- Coroutine cancellation is cooperative. A coroutine code has to cooperate to be cancellable. All the suspending functions in kotlinx.coroutines are cancellable. They check for cancellation of coroutine and throw CancellationException when cancelled. However, if a coroutine is working in a computation and does not check for cancellation, then it cannot be cancelled, like the following example shows:

```launch(Dispatchers.Default) {
    var nextPrintTime = startTime
    var i = 0
    while (i < 5) { // computation loop, just wastes CPU
        // print a message twice a second
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...")
            nextPrintTime += 500L
        }
    }
}
```

- Run it to see that it continues to print "I'm sleeping" even after cancellation until the job completes by itself after five iterations.

- There are two approaches to making computation code cancellable. The first one is periodically invoking a suspending function that checks for cancellation. There are the yield and ensureActive functions, which are great choices for that purpose. The other one is explicitly checking the cancellation status using isActive. Let us try the latter approach.

Replace while (i < 5) in the previous example with while (isActive) and rerun it.

```
val startTime = System.currentTimeMillis()
val job = launch(Dispatchers.Default) {
    var nextPrintTime = startTime
    var i = 0
    while (isActive) { // cancellable computation loop
        // prints a message twice a second
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...")
            nextPrintTime += 500L
        }
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancelAndJoin() // cancels the job and waits for its completion
println("main: Now I can quit.")
```

# Closing resources with finally﻿

- Cancellable suspending functions throw CancellationException on cancellation, which can be handled in the usual way. For example, the try {...} finally {...}


## Run non-cancellable block
Any attempt to use a suspending function in the finally block of the previous example causes CancellationException, because the coroutine running this code is cancelled. Usually, this is not a problem, since all well-behaved closing operations (closing a file, cancelling a job, or closing any kind of communication channel) are usually non-blocking and do not involve any suspending functions. However, in the rare case when you need to suspend in a cancelled coroutine you can wrap the corresponding code in withContext(NonCancellable) {...}

```
 runBlocking {
        val job = launch {
            repeat(1000) { // We can also do while(active) to check cancellation
                println("I am running.. ${it}")
                // this is important as this checks if coroutine has been cancelled
                delay(500L)
            }
        }
        delay(1300L)
        println("I am tired of wating")
        job.cancel()
        println("I am quiting....")
 }
```

## TimeOut

- Wrap in withTimeout
- It throws TimeoutCancellationException which is child of CancellationException. 
- If running within another coroutinescope then it this silently fails as CancellationException is normal failure for coroutine. 

```
 runBlocking {
      withTimeout(500) {
          launch {
              repeat(1000) {
                  println("I am running ... ${it}")
                  delay(200)
              }
          }
      }
  }
}
```

- Timeout event in withTimeout is async that means it can even happen just before withTimeout returns. So close resources if required in finally 
