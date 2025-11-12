package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun main() {
 runBlocking {
     (1..5).asFlow().map { request -> perform(request) }.collect{
         println("Logs $it")
     }
 }
}

suspend fun perform(request: Int): String {
    delay(1000)
    return "Request: $request Response: ${2*request}"
}

