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

    private fun msg(user: String, message: String) = ChatFeedEvent.MessageEvent(user, message)
}