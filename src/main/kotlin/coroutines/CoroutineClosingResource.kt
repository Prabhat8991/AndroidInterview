package coroutines

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val job = launch {
            try {
                repeat(100) {
                    println("I am running ... ${it}")
                    delay(1000)
                }
            } finally {
                //Use this to run suspend functions in finally block
                withContext(NonCancellable) {
                    delay(1000)
                    println("I am finally closing")
                }
            }
        }
        delay(2000)
        job.cancelAndJoin()
        println("I am quitting....")
    }
}