package coroutines

import kotlinx.coroutines.*

fun main() {
   runBlocking {
       val job = launch {
           launch(Job()) {
               println("I am independent and not impact by parents job cancellation")
               delay(5000)
               println("I will still print this if parent is cancelled")
           }

           launch {
               println("I will also be cancelled if parents job is cancellation")
               delay(5000)
               println("I will not print this if parent is cancelled")
           }

       }
       delay(3000)

       job.cancelAndJoin()
       println("Parent job is cancelled")
       delay(5000)
   }
}

