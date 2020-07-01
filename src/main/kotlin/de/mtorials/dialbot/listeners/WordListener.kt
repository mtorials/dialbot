package de.mtorials.dialbot.listeners

import de.mtorials.dialbot.Config
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.listener.ListenerAdapter

class WordListener(
    private val moderationConfig : Config.Moderation
) : ListenerAdapter() {
    override suspend fun onRoomMessageReceive(event: MessageReceivedEvent) {
        for (mod in moderationConfig.rooms) {
            if (mod.roomId != event.roomFuture.id) continue
            if (!mod.filter.enable) return
            mod.filter.words.forEach {
                if (event.content.body.contains(it)) {
                    delete(event)
                    return
                }
            }
        }
    }

    private fun delete(event: MessageReceivedEvent) {
        println("Here it should be deleted!")
    }
}