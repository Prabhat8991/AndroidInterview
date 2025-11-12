# Dispatchers

- The coroutine context includes a coroutine dispatcher (see CoroutineDispatcher) that determines what thread or threads the corresponding coroutine uses for its execution. 

- The coroutine dispatcher can confine coroutine execution to a specific thread, dispatch it to a thread pool, or let it run unconfined.

- All coroutine builders like launch and async accept an optional CoroutineContext parameter that can be used to explicitly specify the dispatcher for the new coroutine and other context elements.

```
launch { // context of the parent, main runBlocking coroutine
    println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
    println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher 
    println("Default               : I'm working in thread ${Thread.currentThread().name}")
}
launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
    println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
}
```

# Job in the context

- The coroutine's Job is part of its context, and can be retrieved from it using the coroutineContext[Job] expression

```
println("My job is ${coroutineContext[Job]}")
```

# Children of a coroutine

- When a coroutine is launched in the CoroutineScope of another coroutine, it inherits its context via CoroutineScope.coroutineContext and the Job of the new coroutine becomes a child of the parent coroutine's job. 

- When the parent coroutine is cancelled, all its children are recursively cancelled, too.

* However, this parent-child relation can be explicitly overridden in one of two ways:
   
   * When a different scope is explicitly specified when launching a coroutine (for example, GlobalScope.launch), it does not inherit a Job from the parent scope.

   * When a different Job object is passed as the context for the new coroutine (as shown in the example below), it overrides the Job of the parent scope.

   ```
   val request = launch {
    // it spawns two other jobs
    launch(Job()) { 
        println("job1: I run in my own Job and execute independently!")
        delay(1000)
        println("job1: I am not affected by cancellation of the request")
    }
    // and the other inherits the parent context
    launch {
        delay(100)
        println("job2: I am a child of the request coroutine")
        delay(1000)
        println("job2: I will not execute this line if my parent request is cancelled")
    }
   }
   delay(500)
   request.cancel() // cancel processing of the request
   println("main: Who has survived request cancellation?")
   delay(1000) // delay the main thread for a second to see what happens 
   ```

 - The parent coroutine automatically waits for all its children to finish before it completes.

 ```
 runBlocking {
    launch {
        delay(1000)
        println("Task from launch 1 done")
    }

    launch {
        delay(500)
        println("Task from launch 2 done")
    }

    println("Parent coroutine waiting for children...")
}

output:

Parent coroutine waiting for children...
Task from launch 2 done
Task from launch 1 done

 ```

 - if structured concurrency didn’t exist, you’d have to write something like this:

 ```
 runBlocking {
    val job1 = launch { delay(1000) }
    val job2 = launch { delay(500) }

    // Manually wait
    job1.join()
    job2.join()
}
 ```

- Combining context elements

```
launch(Dispatchers.Default + CoroutineName("test")) {
    println("I'm working in thread ${Thread.currentThread().name}")
}
```


