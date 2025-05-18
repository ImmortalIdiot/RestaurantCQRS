package com.immortalidiot.command.handler

import com.immortalidiot.command.command.AddDishToOrderCommand
import com.immortalidiot.command.model.OrderItem
import com.immortalidiot.command.repository.OrderRepository
import com.immortalidiot.common.exception.OrderExceptions

class AddDishCommandHandler(
    private val orderRepository: OrderRepository
) : CommandHandler<AddDishToOrderCommand> {

    override fun handle(command: AddDishToOrderCommand) {
        val order = orderRepository.findById(command.orderId)
            ?: throw OrderExceptions.OrderNotFoundException(command.orderId)

        val orderItem = OrderItem(
            dish = command.dish,
            quantity = command.quantity,
            price = command.price
        )

        order.addItem(orderItem)
        orderRepository.save(order)
    }
}
