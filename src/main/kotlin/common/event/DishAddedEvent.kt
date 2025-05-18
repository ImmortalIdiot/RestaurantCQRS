package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class DishAddedEvent(
    val orderId: String,
    val dish: String,
    val quantity: Int = 1,
    val price: Double
) : Event() {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
        require(dish.isNotBlank()) { throw OrderExceptions.BlankDishException() }
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
