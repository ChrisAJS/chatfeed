package uk.sawcz.ircclient.irc

object IRCEventTranslator {
    fun translate(eventString: String): IRCEvent {
        val tags = mutableMapOf<String, String>()
        var targetEventString = eventString
        if (targetEventString.startsWith("@")) {
            targetEventString = targetEventString.drop(1)

            val tagString = targetEventString.substring(0, targetEventString.indexOf(" :"))
            tagString.split(";").forEach {
                val (name, value) = it.split("=")
                tags[name] = value
            }

            targetEventString = targetEventString.drop(tagString.length+1)
        }
        val (source, event, target) = targetEventString.split(" ")

        return when (event) {
            "JOIN" -> {
                val (nick, host) = source.drop(1).split("!")
                IRCEvent.JoinEvent(nick, host, target)
            }
            "PART" -> {
                val (nick, host) = source.drop(1).split("!")
                IRCEvent.PartEvent(nick, host, target)
            }
            "PRIVMSG" -> {
                val (nick, host) = source.drop(1).split("!")
                IRCEvent.PrivateMessageEvent(nick, host, target, targetEventString.drop(1).dropWhile { it != ':' }.substring(1), tags)
            }
            else -> IRCEvent.UnknownEvent(eventString)
        }
    }
}
