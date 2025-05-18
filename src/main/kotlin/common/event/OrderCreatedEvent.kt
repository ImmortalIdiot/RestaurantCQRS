package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class OrderCreatedEvent(
    val orderId: String,
    val tableNumber: Int,
    val customerId: String
) : Event() {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(tableNumber > 0) { throw OrderExceptions.InvalidTableNumberException(tableNumber) }
        require(customerId.isNotBlank()) { throw OrderExceptions.BlankCustomerIdException() }
    }
}
