package coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    runBlocking {
        simple().collect{
            println("Collecting from simple $it")
        }
    }
}

fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
}.flowOn(Dispatchers.Default)