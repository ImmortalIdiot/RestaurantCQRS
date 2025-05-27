package com.immortalidiot.command.handler

import com.immortalidiot.command.command.RemoveDishFromOrderCommand
import com.immortalidiot.command.repository.OrderRepository
import com.immortalidiot.common.exception.OrderExceptions

class RemoveDishCommandHandler(
    private val orderRepository: OrderRepository
) : CommandHandler<RemoveDishFromOrderCommand> {

    override fun handle(command: RemoveDishFromOrderCommand) {
        val order = orderRepository.findById(command.orderId)
            ?: throw OrderExceptions.OrderNotFoundException(command.orderId)

        order.removeItemByDish(command.dish, command.quantity)

        orderRepository.save(order)
    }
}
