import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    coroutineScope {
        launch {
            delay(2000)
            println("Coroutine 1")
        }
        launch {
            delay(2000)
            println("Coroutine 2")
        }
    }
}