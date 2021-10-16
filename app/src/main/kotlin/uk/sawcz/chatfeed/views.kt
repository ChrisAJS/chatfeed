package uk.sawcz.chatfeed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.asDesktopBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@Composable
fun TextWithShadow(
    text: String,
    fontSize: TextUnit = 16.sp,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = Resources.headingFontFamily,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = Color.White
) {
    Box {
        Text(
            text = text,
            color = Colours.tertiaryBlueColour,
            fontSize = fontSize,
            fontFamily = fontFamily,
            textAlign = textAlign,
            modifier = modifier
                .offset(
                    x = 2.dp,
                    y = 2.dp
                )
                .alpha(0.75f)
        )
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            textAlign = textAlign,
            modifier = modifier
        )
    }
}

@Composable
fun MessageCard(backgroundColour: Color, content: @Composable () -> Unit) {
    Card(
        elevation = 3.dp,
        backgroundColor = backgroundColour,
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        content = content
    )
}

@Composable
fun EmojiAwareText(message: String) {
    val chatMessage = buildAnnotatedString {
        val (messageComponents, emojis) = EmojiDetector.process(message)

        this.append(messageComponents.first())

        for (i in emojis.indices) {
            this.appendInlineContent(emojis[i], emojis[i])
            this.append(messageComponents[i+1])
        }
    }
    Text(
        chatMessage, inlineContent = mapOf(
            "Kappa" to emojiContent("Kappa", "emotes/kappa.png"),
            "LUL" to emojiContent("LUL", "emotes/lul.png"),
            "HeyGuys" to emojiContent("HeyGuys", "emotes/heyguys.png"),
            "CoolStoryBob" to emojiContent("CoolStoryBob", "emotes/bob.png"),
            "4Head" to emojiContent("4Head", "emotes/4head.png"),
            "FrankerZ" to emojiContent("FrankerZ", "emotes/frankerz.png"),
            "Jebaited" to emojiContent("Jebaited", "emotes/jebaited.png"),
            "PJSalt" to emojiContent("PJSalt", "emotes/salt.png"),
            "NotLikeThis" to emojiContent("NotLikeThis", "emotes/notlikethis.png"),
            "SeemsGood" to emojiContent("SeemsGood", "emotes/seemsgood.png"),
            "ResidentSleeper" to emojiContent("ResidentSleeper", "emotes/resident-sleeper.png"),
            "bleedPurple" to emojiContent("bleedPurple", "emotes/bleedpurple.png"),
            "TwitchUnity" to emojiContent("TwitchUnity", "emotes/twitchunity.png"),
            "GivePLZ" to emojiContent("GivePLZ", "emotes/giveplz.png"),
            "CoolCat" to emojiContent("CoolCat", "emotes/coolcat.png"),
            "CurseLit" to emojiContent("CurseLit", "emotes/curselit.png"),
        ), fontFamily = Resources.chatFontFamily, modifier = Modifier.padding(8.dp)
    )
}

private fun emojiContent(emojiName: String, emojiResource: String) = InlineTextContent(Placeholder(32.sp, 32.sp, PlaceholderVerticalAlign.AboveBaseline)) {
    Image(bitmap = useResource(emojiResource, ::loadImageBitmap).asDesktopBitmap().asImageBitmap(), emojiName)
}

@Composable
fun JoinMessage(joinEvent: ChatFeedEvent.JoinEvent) {
    MessageCard(backgroundColour = Colours.secondaryBlueColour) {
        Column(Modifier.padding(16.dp)) {
            TextWithShadow("${joinEvent.user} joined", fontSize = 24.sp)
        }
    }
}

@Composable
fun LeaveMessage(partEvent: ChatFeedEvent.LeaveEvent) {
    MessageCard(Colours.primaryOrangeColour) {
        Column(Modifier.padding(16.dp)) {
            TextWithShadow("${partEvent.user} left", fontSize = 24.sp)
        }
    }
}

@Composable
fun Separator() {
    Box(Modifier.fillMaxWidth(0.9f).height(1.dp).background(Colours.tertiaryBlueColour)) { }
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun ChatMessage(message: ChatFeedEvent.MessageEvent) {
    val textColor = Color(0xFF000000.or(message.handleColour.drop(1).toInt(16).toLong()))
    val (h, s, v)  = textColor.toHsv()

    MessageCard(Color.hsv(h, s/2.0f, v)) {
        Column(Modifier.padding(16.dp)) {

            TextWithShadow(message.user, fontSize = 24.sp)
            EmojiAwareText(message.message)
        }
    }
}

private fun Color.toHsv(): FloatArray {
    val cmax = max(max(red, blue), green)
    val cmin = min(min(red, blue), green)
    val diff = cmax - cmin

    val h = if (cmax == red) {
        (60.0f * ((green - blue) / diff)+360.0f) % 360.0f
    } else if (cmax == green) {
        (60.0f * ((blue - red) / diff)+120.0f) % 360.0f
    } else {
        (60.0f * ((red - green) / diff)+240.0f) % 360.0f
    }

    val s = if (cmax == 0.0f) {
        0.0f
    } else {
        (diff/cmax)
    }

    return floatArrayOf(h, s, cmax)
}
