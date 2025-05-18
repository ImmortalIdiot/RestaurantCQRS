package com.immortalidiot.command.handler

import com.immortalidiot.command.command.CancelOrderCommand
import com.immortalidiot.command.repository.OrderRepository
import com.immortalidiot.common.exception.OrderExceptions

class CancelOrderCommandHandler(
    private val orderRepository: OrderRepository
) : CommandHandler<CancelOrderCommand> {

    override fun handle(command: CancelOrderCommand) {
        val order = orderRepository.findById(command.orderId)
            ?: throw OrderExceptions.OrderNotFoundException(command.orderId)

        order.cancel(command.reason)
        orderRepository.save(order)
    }
}
