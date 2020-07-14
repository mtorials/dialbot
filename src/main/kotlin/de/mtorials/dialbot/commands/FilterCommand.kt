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
            "add" ->{
                moderationRoom.filteredWords.add(parameters[2])
                event answerOk "Added ${parameters[2]} to the blacklist!"
            }
            "remove" -> {
                moderationRoom.filteredWords.remove(parameters[2])
                event answerOk "Removed ${parameters[2]} from the blacklist!"
            }
            "removeAll" -> {
                moderationRoom.filteredWords.clear()
            }
        }
        config.write()
    }
}