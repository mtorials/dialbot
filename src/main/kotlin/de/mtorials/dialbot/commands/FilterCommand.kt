package de.mtorials.dialbot.commands

import de.mtorials.dialbot.Config
import de.mtorials.dialbot.answerFail
import de.mtorials.dialbot.answerOk
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.listener.CommandAdapter

class FilterCommand(private val config: Config) : CommandAdapter("filter") {
    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        if (!config.moderation.enable) {
            event answerFail "Moderation is disabled on this bot instance!"
            return
        }
        if (!config.moderation.roomModerationById.containsKey(event.roomFuture.id)) {
            event answerFail "Moderation is not enabled in this Room!"
            return
        }

        val moderationRoom = config.moderation.roomModerationById[event.roomFuture.id]
            ?: error("Error! Config does not exist!")

        when(parameters[0]) {
            "enable" -> {
                moderationRoom.filter.enable = true
                event answerOk "Enabled!"
            }
            "disable" -> {
                moderationRoom.filter.enable = false
                event answerOk "Disabled!"
            }
            "add" ->{
                moderationRoom.filter.words.add(parameters[2])
                event answerOk "Added ${parameters[2]} to the blacklist!"
            }
            "remove" -> {
                moderationRoom.filter.words.remove(parameters[2])
                event answerOk "Removed ${parameters[2]} from the blacklist!"
            }
        }
        config.write()
    }
}