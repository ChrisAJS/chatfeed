package uk.sawcz.ircclient

import uk.sawcz.ircclient.irc.*

class IRCClient(private val ircSource: IRCSource) {

    private var onEventCallback: (IRCEvent) -> Unit = {}

    init {
        ircSource.onCommandReceived { command ->

            if (command.startsWith("PING")) {

                ircSource.sendCommand("PONG")
            } else {

                onEventCallback(IRCEventTranslator.translate(command))
            }
        }
    }

    fun onEvent(callback: (IRCEvent) -> Unit) {
        onEventCallback = callback
    }

    fun sendCommand(command: IRCCommand) {
        ircSource.sendCommand(IRCCommandTranslator.translate(command))
    }
}
