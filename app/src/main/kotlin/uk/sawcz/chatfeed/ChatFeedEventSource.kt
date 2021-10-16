package uk.sawcz.chatfeed

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import uk.sawcz.ircclient.IRCClient
import uk.sawcz.ircclient.irc.IRCCommand
import uk.sawcz.ircclient.irc.IRCEvent
import uk.sawcz.ircclient.network.SocketIRCSource

class ChatFeedEventSource(private val onChatFeedEvent: (ChatFeedEvent) -> Unit) {
    init {
        val ircClient = IRCClient(SocketIRCSource("irc.chat.twitch.tv", 6667))
        ircClient.sendCommand(IRCCommand.NickCommand("justinfan${System.currentTimeMillis()}"))
        ircClient.sendCommand(IRCCommand.ShowJoinAndPartCommand)
        ircClient.sendCommand(IRCCommand.EnableTags)
        ircClient.sendCommand(IRCCommand.JoinCommand("#elfeesho"))
        ircClient.onEvent { event ->

            MainScope().launch {

                // This is tested!
                if (event !is IRCEvent.UnknownEvent) {
                    onChatFeedEvent(IRCChatFeedEventTranslator.translate(event))
                }

            }
        }
    }
}