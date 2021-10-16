package uk.sawcz.ircclient.irc

sealed class IRCEvent {
    data class JoinEvent(val nick: String, val host: String, val channel: String) : IRCEvent()
    data class PartEvent(val nick: String, val host: String, val channel: String) : IRCEvent()
    data class PrivateMessageEvent(val nick: String, val host: String, val channel: String, val message: String, val tags: Map<String, String> = emptyMap()) : IRCEvent()
    data class UnknownEvent(val rawMessage: String) : IRCEvent()
}
