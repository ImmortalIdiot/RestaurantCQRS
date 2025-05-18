package com.immortalidiot.command.model

import com.immortalidiot.common.exception.OrderExceptions
import java.util.*

class OrderItem private constructor(
    private val id: String,
    private val dish: String,
    private val quantity: Int,
    private val price: Double
) {
    constructor(dish: String, quantity: Int, price: Double) : this(
        id = UUID.randomUUID().toString(),
        dish = dish,
        quantity = quantity,
        price = price
    ) {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
        require(dish.isNotBlank()) { throw OrderExceptions.BlankDishException() }
    }

    fun getId(): String = id
    fun getDish(): String = dish
    fun getQuantity(): Int = quantity
    fun getPrice(): Double = price
}
