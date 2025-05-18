package com.immortalidiot.common.event

import java.time.LocalDateTime
import java.util.UUID

abstract class Event {
    val eventId: String = UUID.randomUUID().toString()
    val timestamp: LocalDateTime = LocalDateTime.now()
}
