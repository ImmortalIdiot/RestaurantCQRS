package com.immortalidiot.command.command

import com.immortalidiot.command.model.Dish
import com.immortalidiot.common.exception.OrderExceptions

data class AddDishToOrderCommand(
    val orderId: String,
    val dish: Dish,
    val quantity: Int,
) : Command {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
