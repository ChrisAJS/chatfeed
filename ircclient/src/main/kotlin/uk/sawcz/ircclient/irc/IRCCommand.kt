package uk.sawcz.ircclient.irc

sealed class IRCCommand {
    data class NickCommand(val nick: String) : IRCCommand()
    data class PassCommand(val password: String) : IRCCommand()
    data class JoinCommand(val channel: String) : IRCCommand()
    data class PrivateMessageCommand(val channel: String, val message: String) : IRCCommand()
    object ShowJoinAndPartCommand : IRCCommand()
    object EnableTags : IRCCommand()
}