package de.mtorials.dialbot.commands

import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.dialevents.answer
import de.mtorials.dialphone.listener.CommandAdapter

class PingCommand : CommandAdapter("ping") {
    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        event answer "pong!"
    }
}