package com.immortalidiot.command.model

import com.immortalidiot.common.event.*
import com.immortalidiot.common.exception.OrderExceptions
import java.time.LocalDateTime
import java.util.*

class Order private constructor(
    private val id: String,
    private val customerId: String,
    private val tableNumber: Int,
    private val items: MutableList<OrderItem> = mutableListOf(),
    private var status: OrderStatus = OrderStatus.CREATED,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
    private var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(customerId: String, tableNumber: Int) : this(
        id = UUID.randomUUID().toString(),
        customerId = customerId,
        tableNumber = tableNumber
    ) {
        require(tableNumber > 0) { throw OrderExceptions.InvalidTableNumberException(tableNumber) }
        require(customerId.isNotBlank()) { throw OrderExceptions.BlankCustomerIdException() }

        EventBus.getInstance().publish(
            OrderCreatedEvent(
                orderId = id,
                customerId = customerId,
                tableNumber = tableNumber
            )
        )
    }

    fun getId(): String = id
    fun getCustomerId(): String = customerId
    fun getTableNumber(): Int = tableNumber
    fun getItems(): List<OrderItem> = items.toList()
    fun getStatus(): OrderStatus = status
    fun getCreatedAt(): LocalDateTime = createdAt
    fun getUpdatedAt(): LocalDateTime = updatedAt
    fun getTotalAmount(): Double = items.sumOf { it.getPrice() * it.getQuantity() }

    fun addItem(item: OrderItem) {
        validateOrderModifiable()
        items.add(item)
        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            DishAddedEvent(
                orderId = id,
                dish = item.getDish(),
                quantity = item.getQuantity(),
                price = item.getPrice()
            )
        )
    }

    fun removeItem(itemId: String) {
        validateOrderModifiable()
        val removed = items.removeIf { it.getId() == itemId }
        if (!removed) {
            throw OrderExceptions.OrderItemNotFoundException(itemId)
        }
        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            DishRemovedEvent(
                orderId = id,
                orderItemId = itemId
            )
        )
    }

    fun removeItemByDish(dish: String) {
        validateOrderModifiable()
        val item = items.find { it.getDish() == dish }
            ?: throw OrderExceptions.OrderItemNotFoundException("Dish $dish not found in order")
        
        val itemId = item.getId()
        items.remove(item)
        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            DishRemovedEvent(
                orderId = id,
                orderItemId = itemId
            )
        )
    }

    fun updateStatus(newStatus: OrderStatus) {
        status.validateOrderStatusTransition(newStatus)
        status = newStatus
        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            OrderStatusChangedEvent(
                orderId = id,
                newStatus = newStatus
            )
        )
    }

    fun cancel(reason: String) {
        require(reason.isNotBlank()) { throw OrderExceptions.BlankCancellationReasonException() }
        updateStatus(OrderStatus.CANCELLED)

        EventBus.getInstance().publish(
            OrderCancelledEvent(
                orderId = id,
                reason = reason
            )
        )
    }

    private fun validateOrderModifiable() {
        require(status == OrderStatus.CREATED) {
            throw OrderExceptions.OrderNotModifiableException(status.name)
        }
    }
}
