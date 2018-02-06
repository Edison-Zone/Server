package zone.edison.stream

import kotlinx.coroutines.experimental.delay
import java.io.InputStream

/**
 * @author sergeys
 */
suspend fun InputStream.readNB(): Int {
    while (available() <= 0) {
        delay(100)
    }
    
    return read()
}

suspend fun InputStream.readNB(bytes: ByteArray): Int {
    while (available() <= 0) {
        delay(100)
    }
    
    return read(bytes)
}