package de.mtorials.dialbot.listeners

import de.mtorials.dialbot.Config
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.listener.ListenerAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordListener(
    private val moderationConfig : Config.Moderation
) : ListenerAdapter() {

    override suspend fun onRoomMessageReceive(event: MessageReceivedEvent) {
        if (event.senderId == event.phone.ownId) return
        for ((id, mod) in moderationConfig.roomModerationById) {
            if (id != event.roomFuture.id) continue
            mod.filteredWords.forEach {
                if (event.message.body.contains(it)) {
                    delete(event)
                    return
                }
            }
        }
    }

    private fun delete(event: MessageReceivedEvent) {
        GlobalScope.launch { event.message.redact() }
    }
}