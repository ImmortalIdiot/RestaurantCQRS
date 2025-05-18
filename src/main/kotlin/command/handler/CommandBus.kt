package com.immortalidiot.command.handler

import com.immortalidiot.command.command.*

class CommandBus {
    private val handlers = mutableMapOf<Class<out Command>, CommandHandler<*>>()

    fun <T : Command> register(commandClass: Class<T>, handler: CommandHandler<T>) {
        handlers[commandClass] = handler
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Command> dispatch(command: T) {
        val handler = handlers[command::class.java] as? CommandHandler<T>
            ?: throw IllegalStateException("No handler registered for ${command::class.java.name}")
        handler.handle(command)
    }
}
