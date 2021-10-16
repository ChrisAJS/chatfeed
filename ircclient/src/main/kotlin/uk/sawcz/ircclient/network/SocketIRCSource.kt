package uk.sawcz.ircclient.network

import uk.sawcz.ircclient.irc.IRCSource
import java.net.Socket
import kotlin.concurrent.thread

class SocketIRCSource(host: String, port: Int) : IRCSource {

    private var commandReceivedCallback: (String) -> Unit = {}
    private val clientSocket: Socket

    init {
        clientSocket = Socket(host, port)

        thread {
            var pendingMessage = ""
            val clientStream = clientSocket.getInputStream()
            while (true) {
                val charIn = clientStream.read() // I want to see the manager!
                if (charIn == -1) {
                    // server hung up
                    clientSocket.close()
                    break
                }
                pendingMessage += charIn.toChar()
                if (pendingMessage.endsWith("\r\n")) {
                    commandReceivedCallback(pendingMessage.dropLast(2))
                    pendingMessage = ""
                }
            }
        }
    }

    override fun onCommandReceived(callback: (String) -> Unit) {
        commandReceivedCallback = callback
    }

    override fun sendCommand(command: String) {
        clientSocket.getOutputStream().write((command + "\r\n").toByteArray())
    }

}
