package com.immortalidiot.common.event

import com.immortalidiot.command.model.Dish
import com.immortalidiot.common.exception.OrderExceptions

data class DishAddedEvent(
    val orderId: String,
    val orderItemId: String,
    val dish: Dish,
    val quantity: Int
) : Event() {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(orderItemId.isNotBlank()) { throw OrderExceptions.OrderItemNotFoundException(orderItemId) }
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
    }
}

