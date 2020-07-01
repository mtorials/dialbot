package de.mtorials.dialbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class Config(
    val matrixToken: String = "<YOUR MATRIX ACCOUNT TOKEN>",
    val homeserverUrl: String = "<YOUR_HOMESERVER_URL>",
    val port: Int = 9009,
    val commandPrefix: String = "!",

    val webhooks: Webhooks = Webhooks(),
    val reddit: Reddit = Reddit(),
    val moderation: Moderation = Moderation()
) {
    class Webhooks(
        val token: String = "<YOUR SECRET HERE>",
        val enable: Boolean = false
    )

    class Reddit(
        val enable: Boolean = false,
        val roomToSubreddit: MutableList<RoomToSubReddit> = mutableListOf(RoomToSubReddit())
    ) {
        class RoomToSubReddit(
            val subredditUrl: String = "https://reddit.com/ProgrammerHumor",
            val roomId: String = "!xxx:xxx.com"
        )
    }

    class Moderation(
        val enable: Boolean = false,
        val rooms: MutableList<RoomModeration> = mutableListOf(RoomModeration())
    ) {
        class RoomModeration(
            val roomId: String = "<Your_RoomId>",
            val filter: WordFilter = WordFilter()
        ) {
            class WordFilter(
                val enable: Boolean = true,
                val words: MutableList<String> = mutableListOf("badword", "badword2")
            )
        }
    }

    companion object {
        fun writeConfig(config: Config) {
            File(configFileName).writeText(
                jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(config)
            )
        }
    }
}