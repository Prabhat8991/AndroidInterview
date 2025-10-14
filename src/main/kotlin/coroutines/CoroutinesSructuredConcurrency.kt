import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    coroutineScope {
        this.launch {
            delay(2000)
            println("Coroutine1 completed")
        }
        this.launch {
            delay(5000)
            println("Coroutine2 completed")
        }
    }
    println("Parent coroutine completed")
}

