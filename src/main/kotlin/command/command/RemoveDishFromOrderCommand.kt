package com.immortalidiot.command.command

import com.immortalidiot.command.model.Dish
import com.immortalidiot.common.exception.OrderExceptions

data class RemoveDishFromOrderCommand(
    val orderId: String,
    val dish: Dish,
    val quantity: Int = 1
) : Command {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
    }
}
