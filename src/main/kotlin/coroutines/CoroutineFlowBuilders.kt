package coroutines

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..3).asFlow().collect{
         println("Printing emitted values $it")
        }

        flowOf(1,2,3,4,5).collect{
            println("Printing emitted values from Flow Of $it")
        }
    }

}

