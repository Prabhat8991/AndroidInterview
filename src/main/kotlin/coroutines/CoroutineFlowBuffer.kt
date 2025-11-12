package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

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