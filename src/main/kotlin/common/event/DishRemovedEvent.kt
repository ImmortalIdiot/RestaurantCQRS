package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class DishRemovedEvent(
    val orderId: String,
    val orderItemId: String
) : Event() {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(orderItemId.isNotBlank()) { throw OrderExceptions.OrderItemNotFoundException(orderItemId) }
    }
}
