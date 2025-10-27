package coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

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
