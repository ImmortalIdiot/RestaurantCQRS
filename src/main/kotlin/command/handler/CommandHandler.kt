package com.immortalidiot.command.handler

import com.immortalidiot.command.command.Command

interface CommandHandler<T : Command> {
    fun handle(command: T)
}
