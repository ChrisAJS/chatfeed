package uk.sawcz.chatfeed

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.lang.Math.abs

@Composable
fun App() {

    var messages by remember { mutableStateOf(listOf<ChatFeedEvent>()) }

    val onSuperUserEvent: (String) -> Unit = { event ->
        if (event == "!clear") {
            messages = listOf()
        }
    }

    val messageSeparator = MessageSeparator(SystemClockTimeSource())
    ChatFeedEventSource(onSuperUserEvent) { chatFeedEvent ->
        messages = messageSeparator.separateIfRequired(messages)
        messages = MessageGrouper.group(messages, chatFeedEvent)
    }

    DesktopMaterialTheme {
        Column {
            ChatFeedHeading()

            TwitchChatMessageList(messages)
        }
    }
}

@Composable
fun TwitchChatMessageList(messages: List<ChatFeedEvent>) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(messages) { message ->
            renderChatFeedEvent(message)
        }
    }

    LaunchedEffect(messages) {
        listState.animateScrollToItem(messages.size, 0)
    }
}

@Composable
private fun renderChatFeedEvent(message: ChatFeedEvent) {
    when (message) {
        is ChatFeedEvent.MessageEvent -> ChatMessage(message)
        is ChatFeedEvent.JoinEvent -> JoinMessage(message)
        is ChatFeedEvent.LeaveEvent -> LeaveMessage(message)
        is ChatFeedEvent.Separator -> Separator()
    }
}

fun main() = application {

    Window(
        title = "Chatfeed",
        alwaysOnTop = true,
        state = WindowState(width = 500.dp, height = 1440.dp, position = WindowPosition(Alignment.TopEnd)),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
