package uk.sawcz.chatfeed

import org.junit.Assert.assertEquals
import org.junit.Test

class MessageGrouperTest {

    @Test
    fun willGroupMessagesFromTheSamePersonTogether() {

        val previousChatMessages = listOf(
            msg("A", "A"),
            msg("B", "A"),
            msg("C", "A"),
            msg("A", "B"))
        val newChatLog = MessageGrouper.group(previousChatMessages, msg("A", "C"))

        assertEquals(listOf(
            msg("A", "A"),
            msg("B", "A"),
            msg("C", "A"),
            msg("A", "B\nC")
        ),
        newChatLog)

    }

    @Test
    fun willAppendMessageWhenCannotCombine() {

        val previousChatMessages = listOf(
            msg("A", "A"),
            msg("B", "A"),
            msg("C", "A"),
            msg("A", "B"))
        val newChatLog = MessageGrouper.group(previousChatMessages, msg("B", "B"))

        assertEquals(listOf(
            msg("A", "A"),
            msg("B", "A"),
            msg("C", "A"),
            msg("A", "B"),
            msg("B", "B")
        ),
            newChatLog)
    }

    @Test
    fun willAppendWhenEventListIsEmpty() {
        assertEquals(listOf(msg("A", "A")), MessageGrouper.group(emptyList(), msg("A", "A")))
    }

    @Test
    fun willNotLoseHandleColour() {
        assertEquals(listOf(msg("A", "A\nB", "#ff0000")), MessageGrouper.group(listOf(msg("A", "A", "#ff0000")), msg("A", "B")))
    }

    private fun msg(user: String, message: String, colour: String = "#ffffff") = ChatFeedEvent.MessageEvent(user, message, colour)
}