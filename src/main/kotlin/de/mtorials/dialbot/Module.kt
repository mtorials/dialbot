package de.mtorials.dialbot

import de.mtorials.dialphone.DialPhone

interface Module {
    fun onEnable(phone: DialPhone)
    fun onDisable(phone: DialPhone)
}