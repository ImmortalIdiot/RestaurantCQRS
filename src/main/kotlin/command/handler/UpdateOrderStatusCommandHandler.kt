package com.immortalidiot.command.handler

import com.immortalidiot.command.command.UpdateOrderStatusCommand
import com.immortalidiot.command.repository.OrderRepository
import com.immortalidiot.common.exception.OrderExceptions

class UpdateOrderStatusCommandHandler(
    private val orderRepository: OrderRepository
) : CommandHandler<UpdateOrderStatusCommand> {

    override fun handle(command: UpdateOrderStatusCommand) {
        val order = orderRepository.findById(command.orderId)
            ?: throw OrderExceptions.OrderNotFoundException(command.orderId)

        order.updateStatus(command.newStatus)
        orderRepository.save(order)
    }
}
