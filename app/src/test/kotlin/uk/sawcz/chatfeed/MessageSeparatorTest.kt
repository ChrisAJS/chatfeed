package uk.sawcz.chatfeed

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class MockTimeSource : TimeSource {
    private var timeToReturn: Long = 0L

    override fun time(): Long = timeToReturn

    fun setTimeTo(timeToReturn: Long) {
        this.timeToReturn = timeToReturn
    }
}

class MessageSeparatorTest {

    private val mockTimeSource = MockTimeSource()
    private val messageSeparator = MessageSeparator(mockTimeSource)

    @Test
    fun willNotSeparateWhenOriginalListIsEmpty() {
        val originalList = emptyList<ChatFeedEvent>()

        mockTimeSource.setTimeTo(10_000L)

        val output = messageSeparator.separateIfRequired(originalList)

        assertEquals(emptyList<ChatFeedEvent>(), output)
    }

    @Test
    fun willNotSeparateWhenLessThan5MinutesHasElapsedSinceLastMessage() {
        val originalList = listOf(ChatFeedEvent.MessageEvent("A", "A"))

        mockTimeSource.setTimeTo(TimeUnit.MINUTES.toMillis(5) - 1)

        val output = messageSeparator.separateIfRequired(originalList)

        assertEquals(originalList, output)

        mockTimeSource.setTimeTo(TimeUnit.MINUTES.toMillis(10) - 2)

        val outputAfterSecondEvent = messageSeparator.separateIfRequired(originalList)

        assertEquals(originalList, outputAfterSecondEvent)
    }

    @Test
    fun willSeparateIfNoMessageForOver5Minutes() {

        val originalList = listOf(ChatFeedEvent.MessageEvent("A", "A"))

        mockTimeSource.setTimeTo(TimeUnit.MINUTES.toMillis(5))

        val output = messageSeparator.separateIfRequired(originalList)

        assertEquals(listOf(originalList.first(), ChatFeedEvent.Separator), output)
    }
}