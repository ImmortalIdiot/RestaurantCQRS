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

        val existingItem = orderView.getItems().find { it.dish.name == event.dish.name }

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.quantity + event.quantity)
            existingItem.updatePrice(event.dish.price)
        } else {
            val newItem = OrderItemView(
                id = event.orderItemId,
                dish = event.dish,
                quantity = event.quantity,
                price = event.dish.price
            )
            orderView.addItem(newItem)
        }

        repository.save(orderView)
    }

    private fun handleDishRemoved(event: DishRemovedEvent) {
        val orderView = repository.findById(event.orderId) ?: return
        val item = orderView.getItems().find { it.id == event.orderItemId } ?: return

        val remainingQuantity = item.quantity - event.quantity

        if (remainingQuantity > 0) {
            item.updateQuantity(remainingQuantity)
        } else {
            orderView.removeItem(item.id)
        }

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
