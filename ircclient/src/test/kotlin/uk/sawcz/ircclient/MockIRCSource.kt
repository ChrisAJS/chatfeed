package uk.sawcz.ircclient

import uk.sawcz.ircclient.irc.IRCSource

class MockIRCSource : IRCSource {
    private var commandReceivedCallback: (String) -> Unit = {}
    val capturedSentCommands = mutableListOf<String>()

    fun simulateDataIn(commandReceived: String) {

        commandReceivedCallback(commandReceived)
    }

    override fun onCommandReceived(callback: (String) -> Unit) {

        commandReceivedCallback = callback
    }

    override fun sendCommand(command: String) {
        capturedSentCommands += command
    }
}