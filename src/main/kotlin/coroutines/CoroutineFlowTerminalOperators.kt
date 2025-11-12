package coroutines

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking

/**
 * 1 + 4 + 9 + 16 + 25
 */
fun main() {
    runBlocking {
        val sum = (1..5).asFlow().map {
             it*it
        }.reduce { a, b ->  a + b}

        println("Sum is $sum")
    }
}