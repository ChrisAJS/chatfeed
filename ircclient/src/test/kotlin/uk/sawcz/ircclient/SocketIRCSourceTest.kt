package uk.sawcz.ircclient

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.sawcz.ircclient.network.SocketIRCSource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class SocketIRCSourceTest {

    @Test
    fun canSendCommandToServer() {
        val mockIRCServer = MockIRCServer()

        mockIRCServer.start()
        assertTrue("Never began listening for IRC connections", mockIRCServer.listeningLatch.await(1, TimeUnit.SECONDS))

        val socketIRCSource = SocketIRCSource("localhost", 6667)

        assertTrue("Connection not accepted", mockIRCServer.clientConnectedLatch.await(1, TimeUnit.SECONDS))

        socketIRCSource.sendCommand("JOIN #the-chat")

        assertTrue("Message not received by server", mockIRCServer.messageReceivedLatch.await(1, TimeUnit.SECONDS))
    }

    @Test
    fun canReceiveMessageFromServer() {
        val messageReceivedCountDownLatch = CountDownLatch(1)
        val mockIRCServer = MockIRCServer()
        val expectedMessage = ":nick!user@host PRIVMSG #channel :message\r\n"

        mockIRCServer.start()
        assertTrue("Never began listening for IRC connections", mockIRCServer.listeningLatch.await(1, TimeUnit.SECONDS))

        val socketIRCSource = SocketIRCSource("localhost", 6667)
        lateinit var capturedCommand: String
        socketIRCSource.onCommandReceived { command ->
            capturedCommand = command
            messageReceivedCountDownLatch.countDown()
        }

        assertTrue("Connection not accepted", mockIRCServer.clientConnectedLatch.await(1, TimeUnit.SECONDS))

        socketIRCSource.sendCommand("JOIN #the-chat")

        assertTrue("Message not received by server", mockIRCServer.messageReceivedLatch.await(1, TimeUnit.SECONDS))

        mockIRCServer.sendMessage(expectedMessage)

        assertTrue(messageReceivedCountDownLatch.await(1, TimeUnit.SECONDS))
        assertEquals(expectedMessage.dropLast(2), capturedCommand)
    }

}