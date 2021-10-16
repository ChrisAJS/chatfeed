package uk.sawcz.chatfeed

import java.util.concurrent.TimeUnit

interface TimeSource {
    fun time(): Long
}

class SystemClockTimeSource : TimeSource {
    override fun time() = System.currentTimeMillis()
}

class MessageSeparator(private val timeSource: TimeSource) {

    private var lastRequest: Long = timeSource.time()

    fun separateIfRequired(messages: List<ChatFeedEvent>): List<ChatFeedEvent> {
        val lastRequestTime = lastRequest
        lastRequest = timeSource.time()
        if (timeSource.time() - lastRequestTime >= TimeUnit.MINUTES.toMillis(minutesToWaitBeforeSeparator)) {
            return messages + ChatFeedEvent.Separator
        }
        return messages
    }

    companion object {
        const val minutesToWaitBeforeSeparator = 5L
    }
}

object MessageGrouper {
    fun group(messageList: List<ChatFeedEvent>, nextEvent: ChatFeedEvent): List<ChatFeedEvent> {
        if (messageList.isNotEmpty()) {
            if (nextEvent is ChatFeedEvent.MessageEvent) {
                val event = messageList.last()
                if (event is ChatFeedEvent.MessageEvent) {
                    if (event.user == nextEvent.user) {
                        return messageList.dropLast(1) + ChatFeedEvent.MessageEvent(
                            event.user,
                            event.message + "\n" + nextEvent.message
                        )
                    }
                }
            }
        }
        return messageList + nextEvent
    }
}