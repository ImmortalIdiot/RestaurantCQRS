package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class OrderStatusChangedEvent(
    val orderId: String,
    val newStatus: OrderStatus
) : Event() {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
