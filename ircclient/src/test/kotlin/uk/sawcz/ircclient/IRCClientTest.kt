package uk.sawcz.ircclient

import org.junit.Assert.assertEquals
import org.junit.Test
import uk.sawcz.ircclient.irc.*
import java.awt.SystemColor.info

class IRCClientTest {

    private val mockIRCSource = MockIRCSource()

    private val ircClient = IRCClient(mockIRCSource)

    @Test
    fun canRespondToPing() {

        mockIRCSource.simulateDataIn("PING :tmi.twitch.tv")

        assertEquals(listOf("PONG"), mockIRCSource.capturedSentCommands)
    }

    @Test
    fun willEmitEventWhenUserJoins() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }

        val expectedNick = "${System.currentTimeMillis()}"
        val expectedHost = "${System.currentTimeMillis()}@justinfan348934.tmi.twitch.tv"
        val expectedChannel = "#elfeesho${System.currentTimeMillis()}"

        mockIRCSource.simulateDataIn(":$expectedNick!$expectedHost JOIN $expectedChannel")

        assertEquals(
            IRCEvent.JoinEvent(
                expectedNick,
                expectedHost,
                expectedChannel
            ), capturedEvent)
    }

    @Test
    fun willEmitEventWhenUserParts() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }

        val expectedNick = "${System.currentTimeMillis()}"
        val expectedHost = "${System.currentTimeMillis()}@justinfan348934.tmi.twitch.tv"
        val expectedChannel = "#elfeesho${System.currentTimeMillis()}"

        mockIRCSource.simulateDataIn(":$expectedNick!$expectedHost PART $expectedChannel")

        assertEquals(
            IRCEvent.PartEvent(
                expectedNick,
                expectedHost,
                expectedChannel
            ), capturedEvent)
    }

    @Test
    fun willEmitEventWhenUserSendsMessage() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }

        val expectedNick = "${System.currentTimeMillis()}"
        val expectedHost = "${System.currentTimeMillis()}@justinfan348934.tmi.twitch.tv"
        val expectedChannel = "#elfeesho${System.currentTimeMillis()}"
        val expectedMessage = "some message that's awesome ${System.currentTimeMillis()}"

        mockIRCSource.simulateDataIn(":$expectedNick!$expectedHost PRIVMSG $expectedChannel :$expectedMessage")

        assertEquals(
            IRCEvent.PrivateMessageEvent(
                expectedNick,
                expectedHost,
                expectedChannel,
                expectedMessage
            ), capturedEvent)
    }

    @Test
    fun willExposeMessageTags() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }
        val expectedDisplayName = "DN${System.currentTimeMillis()}"
        val expectedTags = "@badge-info=info;display-name=$expectedDisplayName;emotes=emotes;flags=flags"
        val unusedNick = "unused"
        val expectedHost = "unused"
        val expectedChannel = "#unused"
        val expectedMessage = "unused"

        mockIRCSource.simulateDataIn("$expectedTags :$unusedNick!$expectedHost PRIVMSG $expectedChannel :$expectedMessage")

        assertEquals(
            IRCEvent.PrivateMessageEvent(
                expectedDisplayName,
                expectedHost,
                expectedChannel,
                expectedMessage,
                mapOf("badge-info" to "info", "display-name" to expectedDisplayName, "emotes" to "emotes", "flags" to "flags")
            ), capturedEvent)
    }

    @Test
    fun willEmitEventWhenUserSendsMessageWithTagsUsingDisplayNameAsNick() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }
        val expectedDisplayName = "DisplayName${System.currentTimeMillis()}"
        val expectedTags = "@badge-info=;display-name=$expectedDisplayName;emotes=;flags="
        val unusedNick = "${System.currentTimeMillis()}"
        val expectedHost = "${System.currentTimeMillis()}@justinfan348934.tmi.twitch.tv"
        val expectedChannel = "#elfeesho${System.currentTimeMillis()}"
        val expectedMessage = "some message that's awesome ${System.currentTimeMillis()}"

        mockIRCSource.simulateDataIn("$expectedTags :$unusedNick!$expectedHost PRIVMSG $expectedChannel :$expectedMessage")

        assertEquals(
            IRCEvent.PrivateMessageEvent(
                expectedDisplayName,
                expectedHost,
                expectedChannel,
                expectedMessage
            ), capturedEvent)
    }

    @Test
    fun willDumpUnknownEvents() {

        lateinit var capturedEvent: IRCEvent

        ircClient.onEvent { event ->
            capturedEvent = event
        }

        val unknownCommandText = ":${System.currentTimeMillis()}!${System.currentTimeMillis()} UNKNOWN ${System.currentTimeMillis()} :${System.currentTimeMillis()}"
        mockIRCSource.simulateDataIn(unknownCommandText)

        assertEquals(IRCEvent.UnknownEvent(unknownCommandText), capturedEvent)
    }

    @Test
    fun canSendJoinCommand() {
        val expectedChannelName = "expectedChannel${System.currentTimeMillis()}"

        ircClient.sendCommand(IRCCommand.JoinCommand(expectedChannelName))

        assertEquals(listOf("JOIN $expectedChannelName"), mockIRCSource.capturedSentCommands)
    }

    @Test
    fun canSendNickCommand() {
        val expectedChannelName = "expectednick${System.currentTimeMillis()}"

        ircClient.sendCommand(IRCCommand.NickCommand(expectedChannelName))

        assertEquals(listOf("NICK $expectedChannelName"), mockIRCSource.capturedSentCommands)
    }

    @Test
    fun canRequestJoinAndPartMessages() {

        ircClient.sendCommand(IRCCommand.ShowJoinAndPartCommand)

        assertEquals(listOf("CAP REQ :twitch.tv/membership"), mockIRCSource.capturedSentCommands)
    }

    @Test
    fun canRequestTags() {

        ircClient.sendCommand(IRCCommand.EnableTags)

        assertEquals(listOf("CAP REQ :twitch.tv/tags"), mockIRCSource.capturedSentCommands)
    }

    @Test
    fun willIgnoreEventsWhenNoEventCallbackAttached() {

        mockIRCSource.simulateDataIn(":${System.currentTimeMillis()}!${System.currentTimeMillis()}@justinfan348934.tmi.twitch.tv JOIN #elfeesho${System.currentTimeMillis()}")

    }
}