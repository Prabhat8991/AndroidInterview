package coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
  runBlocking {
    launch {
      println("I am running on thread - 1 ${Thread.currentThread()}")
    }

    launch(Dispatchers.IO) {
      println("I am running on thread - 2 ${Thread.currentThread()}")
    }

    launch(Dispatchers.Default) {
      println("I am running on thread - 3 ${Thread.currentThread()}")
    }

    launch(newSingleThreadContext("MyOwnThread")) {
      println("I am running on thread - 4 ${Thread.currentThread()}")
    }
  }
}

