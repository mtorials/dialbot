package de.mtorials.dialbot.commands

import de.mtorials.dialbot.Config
import de.mtorials.dialbot.answerFail
import de.mtorials.dialbot.answerOk
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.listener.CommandAdapter

class ModerationCommand(private val config: Config) : CommandAdapter("moderation") {

    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        when (parameters[0]) {
            "initiate" -> {
                if (config.moderation.roomModerationById.containsKey(event.roomFuture.id)) {
                    event answerFail "Moderation for this room is enabled!"
                    return
                }
                config.moderation.roomModerationById[event.roomFuture.id] = Config.Moderation.RoomModeration(
                    filteredWords = mutableListOf()
                )
                event answerOk "Enabled moderation for this room!"
                return
            }
            "remove" -> {
                if (!config.moderation.roomModerationById.containsKey(event.roomFuture.id)) {
                    event answerFail "Moderation for this room is disabled!"
                    return
                }
                config.moderation.roomModerationById.remove(event.roomFuture.id)
                event answerOk "Removed moderation for this room!"
                return
            }
        }
        config.write()
    }
}