package com.immortalidiot.command.command

import com.immortalidiot.common.exception.OrderExceptions

data class AddDishToOrderCommand(
    val orderId: String,
    val dish: String,
    val quantity: Int,
    val price: Double
) : Command {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
        require(dish.isNotBlank()) { throw OrderExceptions.BlankDishException() }
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
