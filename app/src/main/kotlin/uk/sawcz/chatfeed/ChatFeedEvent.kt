package uk.sawcz.chatfeed

sealed class ChatFeedEvent {
    data class MessageEvent(val user: String, val message: String, val handleColour: String = "#ffffff") : ChatFeedEvent()
    data class JoinEvent(val user: String) : ChatFeedEvent()
    data class LeaveEvent(val user: String) : ChatFeedEvent()
    object Separator : ChatFeedEvent()
}