package com.immortalidiot.command.model

import com.immortalidiot.common.exception.OrderExceptions
import java.util.*

class OrderItem private constructor(
    private val id: String,
    private val dish: Dish,
    private var quantity: Int,
    private val price: Double
) {
    constructor(dish: Dish, quantity: Int, price: Double) : this(
        id = UUID.randomUUID().toString(),
        dish = dish,
        quantity = quantity,
        price = price
    ) {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
    }

    fun getId(): String = id
    fun getDish(): Dish = dish
    fun getQuantity(): Int = quantity
    fun getPrice(): Double = price

    fun increaseQuantity(amount: Int) {
        if (amount <= 0) throw OrderExceptions.InvalidQuantityException(amount)
        quantity += amount
    }

    fun decreaseQuantity(amount: Int) {
        require(amount > 0) { throw OrderExceptions.InvalidQuantityException(amount) }
        if (amount > quantity) {
            throw OrderExceptions.InvalidQuantityException(amount)
        }
        quantity -= amount
    }
}
