package uk.sawcz.chatfeed

import org.junit.Assert.assertEquals
import org.junit.Test

class EmojiDetectorTest {

    @Test
    fun willSplitAMessageWithEmojiSeparators() {
        val input = "Kappa, I like Kappa, it's my favourite emoji! Kappa"
        val expectedOutputMessageComponents = listOf("", ", I like ", ", it's my favourite emoji! ", "")
        val expectedEmojis = listOf("Kappa", "Kappa", "Kappa")

        val (actualOutputMessageComponents, actualEmojis) = EmojiDetector.process(input)

        assertEquals(expectedOutputMessageComponents, actualOutputMessageComponents)
        assertEquals(expectedEmojis, actualEmojis)
    }

    @Test
    fun canDetectMultipleEmojis() {
        val input = "LUL and Kappa"
        val expectedOutputMessageComponents = listOf("", " and ", "")
        val expectedEmojis = listOf("LUL", "Kappa")

        val (actualOutputMessageComponents, actualEmojis) = EmojiDetector.process(input)

        assertEquals(expectedOutputMessageComponents, actualOutputMessageComponents)
        assertEquals(expectedEmojis, actualEmojis)
    }

    @Test
    fun canDetectEmojiSpam() {
        val input = "LULKappaLULKappaLULKappaLULKappaLULKappaLUL"
        val expectedOutputMessageComponents = listOf("", "", "", "", "", "", "", "", "", "", "", "")
        val expectedEmojis = listOf("LUL", "Kappa", "LUL", "Kappa", "LUL", "Kappa","LUL", "Kappa", "LUL", "Kappa", "LUL")

        val (actualOutputMessageComponents, actualEmojis) = EmojiDetector.process(input)

        assertEquals(expectedEmojis, actualEmojis)
        assertEquals(expectedOutputMessageComponents, actualOutputMessageComponents)
    }
}