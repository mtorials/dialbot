package de.mtorials.dialbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.mtorials.dialbot.commands.AddWordToFilerCommand
import de.mtorials.dialbot.commands.PingCommand
import de.mtorials.dialbot.listeners.InviteListener
import de.mtorials.dialbot.listeners.WordListener
import de.mtorials.dialbot.reddit.Reddit
import de.mtorials.dialbot.webhooks.startWebhooks
import de.mtorials.dialphone.DialPhoneImpl
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException

val logger = KotlinLogging.logger {}
const val configFileName = "config2.json"

fun main() {

    val config: Config
    try {
         config = jacksonObjectMapper().readValue(File(configFileName))
    } catch (e: FileNotFoundException) {
        File(configFileName).writeText(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(Config()))
        return
    }

    val phone = DialPhoneImpl(
        token = config.matrixToken,
        homeserverUrl = config.homeserverUrl,
        commandPrefix = config.commandPrefix,
        listeners = listOf(
            PingCommand(),
            AddWordToFilerCommand(config),
            InviteListener()
        )
    )

    if (config.moderation.enable) phone.addListener(WordListener(config.moderation))

    if (config.webhooks.enable) {
        logger.info("Starting Webhooks...")
        startWebhooks(phone, config.webhooks.token, config.port)
    }

    if (config.reddit.enable) config.reddit.roomToSubreddit.forEach {
        Reddit(phone, it.subredditUrl, it.roomId)
    }

    runBlocking {
        phone.sync().join()
    }
}