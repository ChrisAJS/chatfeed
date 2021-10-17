package uk.sawcz.ircclient.irc

object IRCCommandTranslator {
    fun translate(command: IRCCommand): String {
        return when(command) {
            is IRCCommand.JoinCommand -> "JOIN ${command.channel}"
            is IRCCommand.NickCommand -> "NICK ${command.nick}"
            is IRCCommand.PassCommand -> "PASS ${command.password}"
            is IRCCommand.PrivateMessageCommand -> "PRIVMSG ${command.channel} :${command.message}"
            is IRCCommand.ShowJoinAndPartCommand -> "CAP REQ :twitch.tv/membership"
            is IRCCommand.EnableTags -> "CAP REQ :twitch.tv/tags"
            else -> TODO("Unhandled command")
        }
    }
}
