package com.immortalidiot.common.event

import com.immortalidiot.command.model.Dish
import com.immortalidiot.common.exception.OrderExceptions

data class DishAddedEvent(
    val orderId: String,
    val dish: Dish,
    val quantity: Int,
) : Event() {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
