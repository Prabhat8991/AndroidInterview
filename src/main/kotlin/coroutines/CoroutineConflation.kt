package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        source().conflate().collect{
            delay(1000)
            println("Collecting value: $it")
        }
    }
}

fun source(): Flow<Int> = flow {
    for(i in 1..10) {
        emit(i)
    }
}