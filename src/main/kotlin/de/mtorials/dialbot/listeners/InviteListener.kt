package de.mtorials.dialbot.listeners

import de.mtorials.dialphone.dialevents.RoomInviteEvent
import de.mtorials.dialphone.listener.ListenerAdapter

class InviteListener : ListenerAdapter() {
    override suspend fun onRoomInvite(event: RoomInviteEvent) {
        event.invitedRoomActions.join()
    }
}