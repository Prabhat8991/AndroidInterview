package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

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