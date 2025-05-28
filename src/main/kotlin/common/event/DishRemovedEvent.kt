package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class DishRemovedEvent(
    val orderId: String,
    val orderItemId: String,
    val quantity: Int
) : Event() {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(orderItemId.isNotBlank()) { throw OrderExceptions.OrderItemNotFoundException(orderItemId) }
    }
}
