package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main() {
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