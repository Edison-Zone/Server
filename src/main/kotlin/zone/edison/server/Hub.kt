package zone.edison.server

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * @author sergeys
 *
 * @constructor Creates a new Hub
 */
class Hub(val client: Socket, val inputStream: InputStream, val outputStream: OutputStream) {
    val writer = outputStream.bufferedWriter()
}