package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

enum class OrderStatus {
    CREATED,
    IN_PROGRESS,
    READY,
    COMPLETED,
    CANCELLED;

    fun validateOrderStatusTransition(newStatus: OrderStatus) {
        val validTransitions = when (this) {
            CREATED -> setOf(IN_PROGRESS, CANCELLED)
            IN_PROGRESS -> setOf(READY, CANCELLED)
            READY -> setOf(COMPLETED, CANCELLED)
            COMPLETED -> emptySet()
            CANCELLED -> setOf(CANCELLED)
        }

        if (newStatus !in validTransitions) {
            throw OrderExceptions.InvalidOrderStatusTransitionException(
                this.name,
                newStatus.name,
                validTransitions.map { it.name }.toSet()
            )
        }
    }
}
