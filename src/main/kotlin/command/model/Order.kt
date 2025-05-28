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

        val existingItem = items.find { it.getDish() == item.getDish() }
        if (existingItem != null) {
            existingItem.increaseQuantity(item.getQuantity())
        } else {
            items.add(item)
        }

        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            DishAddedEvent(
                orderId = id,
                orderItemId = (existingItem ?: item).getId(), // ключевой момент
                dish = item.getDish(),
                quantity = item.getQuantity()
            )
        )
    }

    fun removeItemByDish(dish: Dish, quantityToRemove: Int) {
        validateOrderModifiable()

        val item = items.find { it.getDish() == dish }
            ?: throw OrderExceptions.OrderItemNotFoundException("Dish $dish not found in order")

        val currentQty = item.getQuantity()
        if (quantityToRemove > currentQty) {
            throw OrderExceptions.InvalidQuantityException(quantityToRemove)
        }

        val itemId = item.getId()

        if (quantityToRemove == currentQty) {
            items.remove(item)
        } else {
            item.decreaseQuantity(quantityToRemove)
        }

        updatedAt = LocalDateTime.now()

        EventBus.getInstance().publish(
            DishRemovedEvent(
                orderId = id,
                orderItemId = itemId,
                quantity = quantityToRemove
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
