package com.immortalidiot.command.command

import com.immortalidiot.common.exception.OrderExceptions

data class CreateOrderCommand(
    val customerId: String,
    val tableNumber: Int
) : Command {
    init {
        require(tableNumber > 0) { throw OrderExceptions.InvalidTableNumberException(tableNumber) }
        require(customerId.isNotBlank()) { throw OrderExceptions.BlankCustomerIdException() }
    }
}
