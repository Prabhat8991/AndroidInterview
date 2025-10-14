import kotlinx.coroutines.*

suspend fun main() {
   withContext(Dispatchers.Default) {
       this.launch { greet1() }
       this.launch { greet2() }
       this.launch { greet3() }
   }
}

suspend fun greet1() {
    println("Hello wait for it")
}

suspend fun greet2() {
    println("Waiting.....")
    delay(3000)
}

suspend fun greet3() {
    println("Prabhat.....")
}