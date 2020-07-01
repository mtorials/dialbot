package de.mtorials.dialbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.mtorials.dialbot.commands.PingCommand
import de.mtorials.dialbot.listeners.InviteListener
import de.mtorials.dialbot.reddit.Reddit
import de.mtorials.dialbot.webhooks.startWebhooks
import de.mtorials.dialphone.DialPhoneImpl
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.File

val logger = KotlinLogging.logger {}
val config : Config = jacksonObjectMapper().readValue(File("config.json"))

fun main() {

    val phone = DialPhoneImpl(
        token = config.matrixToken,
        homeserverUrl = config.homeserverUrl,
        commandPrefix = config.commandPrefix,
        listeners = listOf(
            PingCommand(),
            InviteListener()
        )
    )

    if (config.webhooks.enable) {
        logger.info("Starting Webhooks...")
        startWebhooks(phone)
    }

    if (config.reddit.enable) config.reddit.roomToSubreddit.forEach {
        Reddit(phone, it.subredditUrl, it.roomId)
    }

    runBlocking {
        phone.sync().join()
    }
}