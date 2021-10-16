package uk.sawcz.chatfeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatFeedHeading() {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Colours.primaryBlueColour, Colours.secondaryBlueColour
                    )
                )
            )
            .padding(16.dp)

    ) {
        HeadingText("twitch.tv/ElFeesho", 30.sp)

        HeadingText("Stream goals", 26.sp)

        HeadingText("Stream Chat", 26.sp)
    }
}

@Composable
private fun HeadingText(text: String, size: TextUnit) {

    TextWithShadow(
        text,
        fontSize = size,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    )
}
