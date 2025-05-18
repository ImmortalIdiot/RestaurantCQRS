package com.immortalidiot.query.dto

data class OrderStatisticsDTO(
    val totalOrders: Int,
    val completedOrders: Int,
    val totalRevenue: Double,
    val averageOrderValue: Double
)
