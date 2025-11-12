package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..5).asFlow().transform {request ->
            emit("Requested $request ")
            emit(performRequest(request))
        }.collect{
            println("Response : $it")
        }
    }
}

suspend fun performRequest(request: Int): String {
    delay(5000)
    return "Request: $request Response: ${2*request}"
}
