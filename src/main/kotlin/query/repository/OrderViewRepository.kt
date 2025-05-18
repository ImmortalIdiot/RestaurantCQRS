package com.immortalidiot.query.repository

import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.query.model.OrderView

class OrderViewRepository {
    private val orderViews: MutableList<OrderView> = mutableListOf()

    fun save(orderView: OrderView) {
        val existingOrderIndex = orderViews.indexOfFirst { it.id == orderView.id }
        if (existingOrderIndex != -1) {
            orderViews[existingOrderIndex] = orderView
        } else {
            orderViews.add(orderView)
        }
    }

    fun findById(id: String): OrderView? = orderViews.find { it.id == id }

    fun findByStatus(status: OrderStatus): List<OrderView> = orderViews.filter { it.status == status }

    fun findByCustomerId(customerId: String): List<OrderView> = orderViews.filter { it.customerId == customerId }

    fun findAll(): List<OrderView> = orderViews.toList()

    fun findByTableNumber(tableNumber: Int): List<OrderView> = orderViews.filter { it.tableNumber == tableNumber }
}