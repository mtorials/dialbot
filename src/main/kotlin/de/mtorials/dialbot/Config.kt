package de.mtorials.dialbot

data class Config(
    val matrixToken: String = "<YOUR MATRIX ACCOUNT TOKEN>",
    val homeserverUrl: String = "<YOUR_HOMESERVER_URL>",
    val port: Int = 9009,
    val commandPrefix: String = "!",

    val webhooks: Webhooks = Webhooks(),
    val reddit: Reddit = Reddit(false, arrayOf())
) {
    class Webhooks(
        val token: String = "<YOUR SECRET HERE>",
        val enable: Boolean = false
    )

    class Reddit(
        val enable: Boolean = false,
        val roomToSubreddit: Array<RoomToSubReddit>
    ) {
        class RoomToSubReddit(
            val subredditUrl: String = "https://reddit.com/ProgrammerHumor",
            val roomId: String = "!YIqYutrrBUdGDombnI:mtorials.de"
        )
    }
}