package com.immortalidiot.query.dto

import java.time.LocalDateTime

data class OrderDTO(
    val id: String,
    val customerId: String,
    val tableNumber: Int,
    val status: String,
    val items: List<OrderItemDTO>,
    val totalAmount: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
