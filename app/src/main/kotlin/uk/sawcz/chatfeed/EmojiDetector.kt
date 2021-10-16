package uk.sawcz.chatfeed

object EmojiDetector {
    fun process(input: String, emojis: List<String> = listOf(
        "Kappa",
        "LUL",
        "HeyGuys",
        "CoolStoryBob",
        "4Head",
        "FrankerZ",
        "Jebaited",
        "PJSalt",
        "NotLikeThis",
        "SeemsGood",
        "ResidentSleeper",
        "bleedPurple",
        "TwitchUnity",
        "GivePLZ",
        "CoolCat",
        "CurseLit"
    )): Pair<List<String>, List<String>> {

        val messageFragments = mutableListOf<String>()
        val foundEmojis = mutableListOf<String>()

        var subject = input

        while (subject.isNotEmpty()) {

            val location = subject.indexOfAny(emojis)
            if (location == -1) {
                break
            }

            messageFragments.add(subject.substring(0 until location))
            subject = subject.drop(location)

            emojis.first { emoji ->
                if (subject.startsWith(emoji)) {
                    foundEmojis.add(emoji)
                    subject = subject.drop(emoji.length)
                    true
                } else {
                    false
                }
            }
        }

        messageFragments.add(subject)

        return messageFragments to foundEmojis
    }
}