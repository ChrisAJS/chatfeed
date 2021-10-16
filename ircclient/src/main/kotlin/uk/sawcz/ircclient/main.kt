package uk.sawcz.ircclient

import uk.sawcz.ircclient.irc.IRCCommand
import uk.sawcz.ircclient.irc.IRCEvent
import uk.sawcz.ircclient.network.SocketIRCSource

fun main() {
    val ircClient = IRCClient(SocketIRCSource("irc.chat.twitch.tv", 6667))

    ircClient.onEvent {
        when (it) {
            is IRCEvent.JoinEvent -> {
                println("< ${it.nick} joined ${it.channel}")
            }
            is IRCEvent.PrivateMessageEvent -> {
                println("< ${it.nick}: ${it.message}")
            }
        }
    }

    ircClient.sendCommand(IRCCommand.NickCommand("justinfan${System.currentTimeMillis()}"))

    ircClient.sendCommand(IRCCommand.JoinCommand("#elfeesho"))

    var exitting = false
    while (!exitting) {
        print("> ")
        val command = readLine()
        when (command) {
            "quit" -> exitting = true
        }
    }
}