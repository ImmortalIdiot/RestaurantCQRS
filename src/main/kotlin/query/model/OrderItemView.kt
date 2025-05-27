package com.immortalidiot.query.model

import com.immortalidiot.command.model.Dish
import com.immortalidiot.common.exception.OrderExceptions
import java.util.*

class OrderItemView(
    val id: String = UUID.randomUUID().toString(),
    val dish: Dish,
    quantity: Int,
    price: Double
) {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
        require(id.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }

    var quantity: Int = quantity
        private set

    var price: Double = price
        private set

    val subtotal: Double
        get() = quantity * price

    fun updateQuantity(newQuantity: Int) {
        require(newQuantity > 0) { throw OrderExceptions.InvalidQuantityException(newQuantity) }
        quantity = newQuantity
    }

    fun updatePrice(newPrice: Double) {
        require(newPrice > 0.0) { throw OrderExceptions.InvalidPriceException(newPrice) }
        price = newPrice
    }
}
