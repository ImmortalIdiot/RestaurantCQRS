package com.immortalidiot.command.command

import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.common.exception.OrderExceptions

data class UpdateOrderStatusCommand(
    val orderId: String,
    val newStatus: OrderStatus
) : Command {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }
}
