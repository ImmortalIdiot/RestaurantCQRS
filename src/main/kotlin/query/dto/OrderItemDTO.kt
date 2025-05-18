package com.immortalidiot.query.dto

data class OrderItemDTO(
    val id: String,
    val dish: String,
    val quantity: Int,
    val price: Double,
    val subtotal: Double
)
