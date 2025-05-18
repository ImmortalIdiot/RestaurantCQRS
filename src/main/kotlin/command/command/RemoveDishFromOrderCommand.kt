package com.immortalidiot.command.command

import com.immortalidiot.common.exception.OrderExceptions

data class RemoveDishFromOrderCommand(
    val orderId: String,
    val dish: String
) : Command {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(dish.isNotBlank()) { throw OrderExceptions.BlankDishException() }
    }
}
