package uk.sawcz.chatfeed

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import uk.sawcz.ircclient.IRCClient
import uk.sawcz.ircclient.irc.IRCCommand
import uk.sawcz.ircclient.irc.IRCEvent
import uk.sawcz.ircclient.network.SocketIRCSource
import java.io.File
import java.io.FileInputStream
import java.util.*

class ChatFeedEventSource(private val onSuperUserEvent: (String) -> Unit, private val onChatFeedEvent: (ChatFeedEvent) -> Unit) {
    init {
        val ircClient = IRCClient(SocketIRCSource("irc.chat.twitch.tv", 6667))

        val credentialsFile = File("${System.getenv("HOME")}/.chatfeedcredentials")

        var eventHandler: (IRCEvent)->Unit = { event ->
            if (event !is IRCEvent.UnknownEvent) {
                MainScope().launch {
                    onChatFeedEvent(IRCChatFeedEventTranslator.translate(event))
                }
            }
        }

        if (credentialsFile.exists()) {
            val properties = Properties().apply {
                load(FileInputStream(credentialsFile))
            }

            val nickName = properties.getProperty("nick")
            val password = properties.getProperty("pass")
            ircClient.sendCommand(IRCCommand.PassCommand(password))
            ircClient.sendCommand(IRCCommand.NickCommand(nickName))

            eventHandler = { event ->
                if (event !is IRCEvent.UnknownEvent) {
                    MainScope().launch {

                        if (event is IRCEvent.PrivateMessageEvent && event.nick == nickName && event.message.startsWith("!")) {
                            // Translate super user events here?
                            onSuperUserEvent(event.message)
                        } else {
                            onChatFeedEvent(IRCChatFeedEventTranslator.translate(event))
                        }
                    }
                }
            }
        } else {
            ircClient.sendCommand(IRCCommand.NickCommand("justinfan${System.currentTimeMillis()}"))
        }

        ircClient.sendCommand(IRCCommand.ShowJoinAndPartCommand)
        ircClient.sendCommand(IRCCommand.EnableTags)
        ircClient.sendCommand(IRCCommand.JoinCommand("#elfeesho"))
        ircClient.onEvent(eventHandler)
    }
}