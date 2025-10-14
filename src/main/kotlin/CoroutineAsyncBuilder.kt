import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

suspend fun main() {
    coroutineScope {
        val deferred1 = async {
            delay(2000)
             3
        }

        val deferred2 = async {
            delay(2000)
            4
        }
        println("Result ${deferred1.await() + deferred2.await()}")
    }
}

