package uk.sawcz.ircclient

import java.net.ServerSocket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MockIRCServer {

    private var messageToSend: String? = null
    val messageReceivedLatch = CountDownLatch(1)
    val messageToSendLatch = CountDownLatch(1)
    val clientConnectedLatch = CountDownLatch(1)
    val listeningLatch = CountDownLatch(1)

    fun start() {
        thread {
            val serverSocket = ServerSocket(6667)
            listeningLatch.countDown()

            val clientSocket = serverSocket.accept()
            clientConnectedLatch.countDown()

            var receivedMessage = ""

            while (!receivedMessage.endsWith("\r\n")) {
                receivedMessage += clientSocket.getInputStream().read().toChar()
            }
            messageReceivedLatch.countDown()
            messageToSendLatch.await(1, TimeUnit.SECONDS)
            messageToSend?.let { message ->
                clientSocket.getOutputStream().write(message.toByteArray())
            }
        }
    }

    fun sendMessage(messageToSend: String) {
        this.messageToSend = messageToSend
        messageToSendLatch.countDown()
    }
}