package de.mtorials.dialbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.mtorials.dialbot.commands.FilterCommand
import de.mtorials.dialbot.commands.ModerationCommand
import de.mtorials.dialbot.commands.PingCommand
import de.mtorials.dialbot.listeners.InviteListener
import de.mtorials.dialbot.listeners.WordListener
import de.mtorials.dialbot.rss.Rss
import de.mtorials.dialbot.webhooks.startWebhooks
import de.mtorials.dialphone.DialPhoneImpl
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException

val logger = KotlinLogging.logger {}
const val configFileName = "config.json"

fun main() {

    val config: Config
    try {
         config = jacksonObjectMapper().readValue(File(configFileName))
    } catch (e: FileNotFoundException) {
        println("generate config file....")
        File(configFileName).writeText(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(Config.getExampleConfig()))
        return
    }

    val phone = DialPhoneImpl(
        token = config.matrixToken,
        homeserverUrl = config.homeserverUrl,
        commandPrefix = config.commandPrefix,
        listeners = listOf(
            PingCommand(),
            FilterCommand(config),
            InviteListener(),
            ModerationCommand(config)
        )
    )

    if (config.moderation.enable) {
        phone.addListener(WordListener(config.moderation))
    }

    if (config.webhooks.enable) {
        logger.info("Starting Webhooks...")
        startWebhooks(phone, config.webhooks.secret, config.port)
    }

    if (config.rss.enable) config.rss.rssUrlByRoomId.forEach { (roomId, url) ->
        Rss(phone, url, roomId, config.rss.updateIntervalMillis)
    }

    runBlocking {
        phone.sync().join()
    }
}