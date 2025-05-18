package com.immortalidiot.query.model

import com.immortalidiot.common.exception.OrderExceptions
import java.util.*

data class OrderItemView(
    val id: String = UUID.randomUUID().toString(),
    val dish: String,
    val quantity: Int,
    val price: Double
) {
    init {
        require(quantity > 0) { throw OrderExceptions.InvalidQuantityException(quantity) }
        require(price > 0.0) { throw OrderExceptions.InvalidPriceException(price) }
        require(dish.isNotBlank()) { throw OrderExceptions.BlankDishException() }
        require(id.isNotBlank()) { throw OrderExceptions.BlankOrderIdException() }
    }

    val subtotal: Double
        get() = quantity * price
}
