package com.immortalidiot.command.handler

import com.immortalidiot.command.command.CreateOrderCommand
import com.immortalidiot.command.model.Order
import com.immortalidiot.command.repository.OrderRepository

class CreateOrderCommandHandler(
    private val orderRepository: OrderRepository
) : CommandHandler<CreateOrderCommand> {

    override fun handle(command: CreateOrderCommand) {
        val order = Order(
            customerId = command.customerId,
            tableNumber = command.tableNumber
        )

        orderRepository.save(order)
    }
}
