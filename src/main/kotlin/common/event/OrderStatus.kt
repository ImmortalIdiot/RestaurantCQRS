package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

enum class OrderStatus(val displayName: String) {
    CREATED("Создан"),
    IN_PROGRESS("Готовится"),
    READY("Готов"),
    COMPLETED("Завершён"),
    CANCELLED("Отменён");

    override fun toString(): String = displayName

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

    fun getValidTransitions(): Set<OrderStatus> = when (this) {
        CREATED -> setOf(IN_PROGRESS, CANCELLED)
        IN_PROGRESS -> setOf(READY, CANCELLED)
        READY -> setOf(COMPLETED, CANCELLED)
        COMPLETED -> emptySet()
        CANCELLED -> setOf(CANCELLED)
    }

    companion object {
        fun fromString(status: String): OrderStatus =
            entries.find { it.name.equals(status, ignoreCase = true) }
                ?: throw IllegalArgumentException("Неизвестный статус: $status")
    }
}

