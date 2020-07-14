package de.mtorials.dialbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class Config(
    val matrixToken: String,
    val homeserverUrl: String,
    val port: Int = 9009,
    val commandPrefix: String,
    val wordLists: MutableMap<String, MutableList<String>>,

    val webhooks: Webhooks,
    val rss: Rss,
    val moderation: Moderation
) {
    class Webhooks(
        val secret: String,
        val enable: Boolean
    )

    class Rss(
        val enable: Boolean,
        val rssUrlByRoomId: MutableMap<String, String>,
        val updateIntervalMillis: Long = 20000L
    )

    class Moderation(
        val enable: Boolean,
        val roomModerationById: MutableMap<String, RoomModeration>
    ) {
        class RoomModeration(
            val filteredWords: MutableList<String>
        )
    }

    fun write() {
        File(configFileName).writeText(
            jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this)
        )
    }

    companion object {
        fun getExampleConfig() = Config(
            matrixToken = "<YOUR_TOKEN>",
            commandPrefix = "!",
            homeserverUrl = "https://xxx:yyy.com",
            port = 9009,
            wordLists = mutableMapOf("<LIST_NAME>" to mutableListOf("badword", "morebad", "reallybad")),
            webhooks = Webhooks(
                enable = false,
                secret = "<SECRET>"
            ),
            rss = Rss(
                enable = false,
                updateIntervalMillis = 20000,
                rssUrlByRoomId = mutableMapOf("<YOUR_ROOMID>" to "https://reddit.com/ProgrammerHumor/new.rss")
            ),
            moderation = Moderation(
                enable = false,
                roomModerationById = mutableMapOf("<YOUR_ROOMID" to Moderation.RoomModeration(
                    filteredWords = mutableListOf("badword", "reallybadword")
                ))
            )
        )

    }
}