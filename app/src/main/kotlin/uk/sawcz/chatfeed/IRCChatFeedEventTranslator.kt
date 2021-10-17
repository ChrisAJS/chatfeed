package uk.sawcz.chatfeed

import uk.sawcz.ircclient.irc.IRCEvent

object IRCChatFeedEventTranslator {
    fun translate(ircEvent: IRCEvent): ChatFeedEvent {
        return when (ircEvent) {
            is IRCEvent.JoinEvent -> ChatFeedEvent.JoinEvent(ircEvent.nick)
            is IRCEvent.PartEvent -> ChatFeedEvent.LeaveEvent(ircEvent.nick)
            is IRCEvent.PrivateMessageEvent -> ChatFeedEvent.MessageEvent(ircEvent.tags.getOrDefault("display-name", ircEvent.nick), ircEvent.message, ircEvent.tags.getOrDefault("color", "#ffffff"))
            is IRCEvent.UnknownEvent -> TODO()
        }
    }
}