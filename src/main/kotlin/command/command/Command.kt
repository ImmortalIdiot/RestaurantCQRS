package com.immortalidiot.command.command

import java.util.UUID

interface Command {
    fun getCommandId(): String = UUID.randomUUID().toString()
}
