package com.immortalidiot.query.model

import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.common.exception.OrderExceptions
import com.immortalidiot.query.dto.OrderDTO
import com.immortalidiot.query.dto.OrderItemDTO
import java.time.LocalDateTime

data class OrderView(
    val id: String,
    val customerId: String,
    val tableNumber: Int,
    private var _status: OrderStatus,
    private val items: MutableList<OrderItemView> = mutableListOf(),
    private var _totalAmount: Double = 0.0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    private var _updatedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(id.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
        require(customerId.isNotBlank()) { throw OrderExceptions.BlankCustomerIdException() }
        require(tableNumber > 0) { throw OrderExceptions.InvalidTableNumberException(tableNumber) }
    }

    val status: OrderStatus get() = _status
    val totalAmount: Double get() = _totalAmount
    val updatedAt: LocalDateTime get() = _updatedAt

    fun getItems(): List<OrderItemView> = items.toList()

    fun addItem(item: OrderItemView) {
        items.add(item)
        recalculateTotalAmount()
        updateTimestamp()
    }

    fun removeItem(itemId: String) {
        items.removeIf { it.id == itemId }
        recalculateTotalAmount()
        updateTimestamp()
    }

    fun updateStatus(newStatus: OrderStatus) {
        _status.validateOrderStatusTransition(newStatus)
        _status = newStatus
        updateTimestamp()
    }

    private fun recalculateTotalAmount() {
        _totalAmount = items.sumOf { it.subtotal }
    }

    private fun updateTimestamp() {
        _updatedAt = LocalDateTime.now()
    }

    fun orderItemListToDTO(): List<OrderItemDTO> = items.map {
        OrderItemDTO(
            id = it.id,
            dish = it.dish,
            quantity = it.quantity,
            price = it.price,
            subtotal = it.subtotal
        )
    }

    fun toDTO(): OrderDTO = OrderDTO(
        id = id,
        customerId = customerId,
        tableNumber = tableNumber,
        status = status.name,
        items = orderItemListToDTO(),
        totalAmount = totalAmount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
