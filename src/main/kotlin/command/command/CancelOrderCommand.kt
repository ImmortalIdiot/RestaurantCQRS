package com.immortalidiot.command.command

import com.immortalidiot.common.exception.OrderExceptions

data class CancelOrderCommand(
    val orderId: String,
    val reason: String
) : Command {
    init {
        require(orderId.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(reason.isNotBlank()) { throw OrderExceptions.BlankCancellationReasonException() }
    }
}
