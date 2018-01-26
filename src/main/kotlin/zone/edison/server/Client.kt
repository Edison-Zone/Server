package zone.edison.server

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * @author sergeys
 */
fun onConnect(isActive: ()->Boolean, client: Socket) {
    val inputStream = client.getInputStream()
    val outputStream = client.getOutputStream()
    
    val firstByte = inputStream.read()
    
    if (firstByte == 0) { //Hub
        onHubConnect(isActive, client, inputStream, outputStream)
    } else { //App
        onAppConnect(isActive, client, inputStream, outputStream)
    }
}

var hub: Hub? = null

fun onHubConnect(isActive: ()->Boolean, client: Socket, inputStream: InputStream, outputStream: OutputStream) {
    hub = Hub(client, inputStream, outputStream)
    
    println("Connected to hub")
    
    while (isActive()) {
        try {
            val read = inputStream.read()
            
            if (read == -1) {
                println("Disconnected from hub")
                hub = null
                break
            }
        } catch (e: EOFException) {
            println("Disconnected from hub")
            hub = null
            break
        }
    }
}

fun onAppConnect(isActive: ()->Boolean, client: Socket, inputStream: InputStream, outputStream: OutputStream) {
    //val inputReader = inputStream.bufferedReader()
    //val outputWriter = outputStream.bufferedWriter()
    
    println("Connected to app")
    
    val bytes = ByteArray(1024)
    
    while (isActive()) {
        try {
            val numBytes = inputStream.read(bytes)
            //val appMessage = gson.fromJson<AppMessage>(inputReader)
    
            if (numBytes == -1) {
                println("Disconnected from app")
                inputStream.close()
                outputStream.close()
                client.close()
                break
            }
    
            hub?.also { hub ->
                //val hubMessage = HubMessage(appMessage.msgId, appMessage.msg)
        
                //gson.toJson<HubMessage>(hubMessage, hub.writer)
    
                hub.outputStream.write(bytes, 0, numBytes)
                hub.outputStream.flush()
            } ?: run {
                println("No hub to send message to. Ignoring.")
                
                //val response = AppMessage(appMessage.hub, appMessage.msgId, "ERROR: NO HUB")
        
                //gson.toJson<AppMessage>(response, outputWriter)
                //outputWriter.flush()
            }
        } catch (e: EOFException) {
            println("Disconnected from app")
            break
        }
    }
}