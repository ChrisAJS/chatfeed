package uk.sawcz.chatfeed

import org.junit.Assert.assertEquals
import org.junit.Test
import uk.sawcz.ircclient.irc.IRCEvent

class IRCChatFeedEventTranslatorTest {

    @Test
    fun willTranslateJoinEvents() {

        assertEquals(ChatFeedEvent.JoinEvent("ElFeesho"), IRCChatFeedEventTranslator.translate(IRCEvent.JoinEvent("ElFeesho", "UNUSED", "UNUSED")))
    }

    @Test
    fun willTranslateLeaveEvents() {

        assertEquals(ChatFeedEvent.LeaveEvent("ElFeesho"), IRCChatFeedEventTranslator.translate(IRCEvent.PartEvent("ElFeesho", "UNUSED", "UNUSED")))
    }

    @Test
    fun willTranslateMessageEvents() {

        assertEquals(ChatFeedEvent.MessageEvent("ElFeesho", "Hello, world"), IRCChatFeedEventTranslator.translate(IRCEvent.PrivateMessageEvent("ElFeesho", "UNUSED", "UNUSED", "Hello, world")))
    }


    @Test
    fun willPullOutColourFromTags() {

        assertEquals(ChatFeedEvent.MessageEvent("ElFeesho", "message", "#ff00ff"), IRCChatFeedEventTranslator.translate(IRCEvent.PrivateMessageEvent("ElFeesho", "UNUSED", "UNUSED", "message", mapOf("color" to "#ff00ff"))))
    }


}