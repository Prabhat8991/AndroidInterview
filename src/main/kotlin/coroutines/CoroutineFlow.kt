package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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