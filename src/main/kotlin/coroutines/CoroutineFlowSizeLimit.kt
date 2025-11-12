package coroutines

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..10).asFlow().take(2).collect{
            println("Collecting from Flow ${it}")
        }
    }
}