package de.mtorials.dialbot

import de.mtorials.dialphone.DialPhone
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun DialPhone.registerOnInterval(intervalMillis: Long, block: (DialPhone) -> Boolean) {
    GlobalScope.launch {
        while(true) {
            if (!block(this@registerOnInterval)) break
            delay(intervalMillis)
        }
    }
}