package uk.sawcz.ircclient.irc

interface IRCSource {
    fun onCommandReceived(callback: (String) -> Unit)
    fun sendCommand(command: String)
}