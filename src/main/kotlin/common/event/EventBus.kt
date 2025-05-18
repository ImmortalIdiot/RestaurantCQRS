package com.immortalidiot.common.event

import java.util.concurrent.CopyOnWriteArrayList


class EventBus private constructor() {
    private val handlers = CopyOnWriteArrayList<EventHandler>()

    fun register(handler: EventHandler) {
        handlers.add(handler)
    }

    fun unregister(handler: EventHandler) {
        handlers.remove(handler)
    }

    fun publish(event: Event) {
        handlers.forEach { handler ->
            handler.handle(event)
        }
    }

    interface EventHandler {
        fun handle(event: Event)
    }

    companion object {
        private val INSTANCE = EventBus()

        fun getInstance(): EventBus = INSTANCE
    }
}
