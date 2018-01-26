@file:JvmName("Main")

package zone.edison.server

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.net.ServerSocket
import java.net.SocketTimeoutException
import java.util.Scanner

/**
 * @author sergeys
 */
//lateinit var gson: Gson

//inline fun <reified T> Gson.fromJson(reader: Reader) = this.fromJson(reader, T::class.java)
//inline fun <reified T> Gson.toJson(obj: Any, writer: Writer) = this.toJson(obj, T::class.java, writer)

fun main(args: Array<String>) = runBlocking {
    
    //gson = GsonBuilder().apply {
    //
    //}.create()
    
    val serverSocketCoroutine = launch(CommonPool) {
        val serverSocket = ServerSocket(11899)
        serverSocket.soTimeout = 1000
        
        while (isActive) {
            try {
                val socket = serverSocket.accept()
                
                launch {
                    onConnect(::isActive, socket)
                }
            } catch (e: SocketTimeoutException) {
            }
        }
    
        serverSocket.close()
    }
    
    val scanner = Scanner(System.`in`)
    
    do {
        println("Type q to quit")
    } while (scanner.nextLine().toLowerCase() != "q")
    
    serverSocketCoroutine.cancel()
    scanner.close()
}