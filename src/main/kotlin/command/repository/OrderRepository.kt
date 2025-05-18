package com.immortalidiot.command.repository

import com.immortalidiot.command.model.Order

interface OrderRepository {
    fun save(order: Order)
    fun findById(id: String): Order?
    fun deleteById(id: String)
}

class InMemoryOrderRepository : OrderRepository {
    private val orders = mutableListOf<Order>()

    override fun save(order: Order) {
        val existingOrderIndex = orders.indexOfFirst { it.getId() == order.getId() }
        if (existingOrderIndex != -1) {
            orders[existingOrderIndex] = order
        } else {
            orders.add(order)
        }
    }

    override fun findById(id: String): Order? =
        orders.find { it.getId() == id }

    override fun deleteById(id: String) {
        orders.removeIf { it.getId() == id }
    }
}
