package de.mtorials.dialbot

import de.mtorials.dialphone.DialPhone
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.dialevents.answer
import de.mtorials.dialphone.sendHtmlMessage
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

suspend infix fun MessageReceivedEvent.answerOk(answer: String) {
    this.roomFuture.sendHtmlMessage("<h3>Ok</h3><br/><p>${answer}</p>")
}

suspend infix fun MessageReceivedEvent.answerFail(answer: String) {
    this.roomFuture.sendHtmlMessage("<h3>Sorry!</h3><br/><p>${answer}</p>")
}