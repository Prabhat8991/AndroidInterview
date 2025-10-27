import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
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
}

