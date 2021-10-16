package uk.sawcz.chatfeed

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font

object Resources {

    val headingFont by lazy { Font("fonts/headingfont.ttf") }
    val headingFontFamily by lazy { FontFamily(headingFont) }

    val chatFont by lazy { Font("fonts/chatfont.ttf") }
    val chatFontFamily by lazy { FontFamily(chatFont) }
}