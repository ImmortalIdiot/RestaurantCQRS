package com.immortalidiot.query.service

import com.immortalidiot.query.dto.OrderDTO
import com.immortalidiot.query.dto.OrderStatisticsDTO
import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.query.repository.OrderViewRepository

class OrderQueryService(private val orderViewRepository: OrderViewRepository) {
    fun getOrderById(id: String): OrderDTO? {
        return orderViewRepository.findById(id)?.toDTO()
    }

    fun getOrdersByStatus(status: OrderStatus): List<OrderDTO> {
        return orderViewRepository.findByStatus(status).map { it.toDTO() }
    }

    fun getOrdersByCustomer(customerId: String): List<OrderDTO> {
        return orderViewRepository.findByCustomerId(customerId).map { it.toDTO() }
    }

    fun getOrdersByTableNumber(tableNumber: Int): List<OrderDTO> {
        return orderViewRepository.findByTableNumber(tableNumber).map { it.toDTO() }
    }

    fun getAllOrders(): List<OrderDTO> {
        return orderViewRepository.findAll().map { it.toDTO() }
    }

    fun getOrderStatistics(): OrderStatisticsDTO {
        val allOrders = orderViewRepository.findAll()
        val completedOrders = orderViewRepository.findByStatus(OrderStatus.COMPLETED)
        val totalRevenue = completedOrders.sumOf { it.totalAmount }

        return OrderStatisticsDTO(
            totalOrders = allOrders.size,
            completedOrders = completedOrders.size,
            totalRevenue = totalRevenue,
            averageOrderValue = if (completedOrders.isNotEmpty()) { totalRevenue / completedOrders.size }
            else 0.0
        )
    }
}
