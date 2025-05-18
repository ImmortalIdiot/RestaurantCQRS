package com.immortalidiot.query.service

import com.immortalidiot.common.event.*
import com.immortalidiot.query.model.OrderItemView
import com.immortalidiot.query.model.OrderView
import com.immortalidiot.query.repository.OrderViewRepository

class OrderEventHandler(
    private val repository: OrderViewRepository
) : EventBus.EventHandler {

    override fun handle(event: Event) {
        when (event) {
            is OrderCreatedEvent -> handleOrderCreated(event)
            is DishAddedEvent -> handleDishAdded(event)
            is DishRemovedEvent -> handleDishRemoved(event)
            is OrderCancelledEvent -> handleOrderCancelled(event)
            is OrderStatusChangedEvent -> handleOrderStatusChanged(event)
            else -> {
                // неизвестный тип события
            }
        }
    }

    private fun handleOrderCreated(event: OrderCreatedEvent) {
        val orderView = OrderView(
            id = event.orderId,
            customerId = event.customerId,
            tableNumber = event.tableNumber,
            _status = OrderStatus.CREATED
        )
        repository.save(orderView)
    }

    private fun handleDishAdded(event: DishAddedEvent) {
        val orderView = repository.findById(event.orderId) ?: return

        val existingItem = orderView.getItems().find { it.dish == event.dish }

        if (existingItem != null) {
            orderView.removeItem(existingItem.id)
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + event.quantity,
                price = event.price
            )
            orderView.addItem(updatedItem)
        } else {
            val newItem = OrderItemView(
                dish = event.dish,
                quantity = event.quantity,
                price = event.price
            )
            orderView.addItem(newItem)
        }

        repository.save(orderView)
    }

    private fun handleDishRemoved(event: DishRemovedEvent) {
        val orderView = repository.findById(event.orderId) ?: return
        orderView.removeItem(event.orderItemId)
        repository.save(orderView)
    }

    private fun handleOrderCancelled(event: OrderCancelledEvent) {
        val orderView = repository.findById(event.orderId) ?: return
        orderView.updateStatus(OrderStatus.CANCELLED)
        repository.save(orderView)
    }

    private fun handleOrderStatusChanged(event: OrderStatusChangedEvent) {
        val orderView = repository.findById(event.orderId) ?: return
        orderView.updateStatus(event.newStatus)
        repository.save(orderView)
    }
}
