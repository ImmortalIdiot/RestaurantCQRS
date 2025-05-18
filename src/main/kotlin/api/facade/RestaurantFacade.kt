package com.immortalidiot.api.facade

import com.immortalidiot.command.command.*
import com.immortalidiot.command.handler.CommandBus
import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.query.dto.OrderDTO
import com.immortalidiot.query.dto.OrderStatisticsDTO
import com.immortalidiot.query.service.OrderQueryService

class RestaurantFacade(
    private val commandBus: CommandBus,
    private val queryService: OrderQueryService
) {
    // Command operations
    fun createOrder(customerId: String, tableNumber: Int) {
        commandBus.dispatch(CreateOrderCommand(customerId, tableNumber))
    }

    fun addDishToOrder(orderId: String, dish: String, quantity: Int, price: Double) {
        commandBus.dispatch(AddDishToOrderCommand(orderId, dish, quantity, price))
    }

    fun removeDishFromOrder(orderId: String, dish: String) {
        commandBus.dispatch(RemoveDishFromOrderCommand(orderId, dish))
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        commandBus.dispatch(UpdateOrderStatusCommand(orderId, newStatus))
    }

    fun cancelOrder(orderId: String, reason: String) {
        commandBus.dispatch(CancelOrderCommand(orderId, reason))
    }

    // Query operations
    fun getOrder(orderId: String): OrderDTO? {
        return queryService.getOrderById(orderId)
    }

    fun getAllOrders(): List<OrderDTO> {
        return queryService.getAllOrders()
    }

    fun getOrdersByStatus(status: OrderStatus): List<OrderDTO> {
        return queryService.getOrdersByStatus(status)
    }

    fun getOrdersByCustomer(customerId: String): List<OrderDTO> {
        return queryService.getOrdersByCustomer(customerId)
    }

    fun getOrdersByTableNumber(tableNumber: Int): List<OrderDTO> {
        return queryService.getOrdersByTableNumber(tableNumber)
    }

    fun getOrderStatistics(): OrderStatisticsDTO {
        return queryService.getOrderStatistics()
    }
}
