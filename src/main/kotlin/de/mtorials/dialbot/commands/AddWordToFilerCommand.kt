package de.mtorials.dialbot.commands

import de.mtorials.dialbot.Config
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.dialevents.answer
import de.mtorials.dialphone.listener.CommandAdapter

class AddWordToFilerCommand(private val config: Config) : CommandAdapter("addToFilter") {
    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        event answer "Ok!"
        config.moderation.rooms.filter { roomModeration -> roomModeration.roomId == event.roomFuture.id }[0].filter.words.add("aaaaaa")
        Config.writeConfig(config)
    }
}