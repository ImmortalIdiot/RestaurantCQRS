package com.immortalidiot.common.event

import com.immortalidiot.common.exception.OrderExceptions

data class OrderCancelledEvent(
    val orderId: String,
    val reason: String
) : Event() {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(reason.isNotBlank()) { throw OrderExceptions.BlankCancellationReasonException() }
    }
}
