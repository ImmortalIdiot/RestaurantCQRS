package com.immortalidiot.api.ui

import com.immortalidiot.api.facade.RestaurantFacade
import com.immortalidiot.common.event.OrderStatus
import java.time.format.DateTimeFormatter
import java.util.*

class ConsoleInterface(private val restaurantFacade: RestaurantFacade) {
    private val scanner = Scanner(System.`in`)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    fun start() {
        var choice: Int
        do {
            showMainMenu()
            choice = readIntInput()
            scanner.nextLine()
            handleMainMenuChoice(choice)
        } while (choice != 0)
    }

    private fun showMainMenu() {
        println("\n===== Система управления заказами ресторана =====")
        println("1. Создать новый заказ")
        println("2. Показать все заказы")
        println("3. Информация о заказе")
        println("4. Добавить блюдо в заказ")
        println("5. Удалить блюдо из заказа")
        println("6. Обновить статус заказа")
        println("7. Отменить заказ")
        println("8. Статистика заказов")
        println("0. Выход")
        print("Выберите действие: ")
    }

    private fun handleMainMenuChoice(choice: Int) {
        try {
            when (choice) {
                0 -> println("Выход из программы...")
                1 -> handleCreateOrder()
                2 -> showAllOrders()
                3 -> showOrderDetails()
                4 -> addDishToOrder()
                5 -> removeDishFromOrder()
                6 -> updateOrderStatus()
                7 -> cancelOrder()
                8 -> showOrderStatistics()
                else -> println("Неверный выбор. Попробуйте снова.")
            }
        } catch (e: Exception) {
            println("Произошла ошибка: ${e.message}")
        }
    }

    private fun handleCreateOrder() {
        print("Введите id клиента: ")
        val customerId = readlnOrNull() ?: return

        print("Введите номер столика: ")
        val tableNumber = readlnOrNull()?.toIntOrNull() ?: return

        try {
            restaurantFacade.createOrder(customerId, tableNumber)
            val orders = restaurantFacade.getAllOrders()
            val orderId = orders.last().id

            while (true) {
                print("\nДобавить блюдо в заказ? (Да/Нет): ")
                when (readlnOrNull()?.lowercase()) {
                    "да" -> {
                        print("Введите название блюда: ")
                        val dish = readlnOrNull() ?: continue

                        print("Введите количество: ")
                        val quantity = readlnOrNull()?.toIntOrNull() ?: continue

                        print("Введите цену: ")
                        val price = readlnOrNull()?.toDoubleOrNull() ?: continue

                        try {
                            restaurantFacade.addDishToOrder(orderId, dish, quantity, price)
                            println("Блюдо успешно добавлено в заказ!")
                        } catch (e: Exception) {
                            println("Ошибка добавления блюда в заказ: ${e.message}")
                        }
                    }
                    "нет" -> break
                    else -> println("Неверный ввод. Введите 'Да' или 'Нет'.")
                }
            }

            println("\nЗаказ успешно создан!")
            printOrderDetails(orderId)
        } catch (e: Exception) {
            println("Ошибка создания заказа: ${e.message}")
        }
    }

    private fun printOrderDetails(orderId: String) {
        val order = restaurantFacade.getOrder(orderId)
        if (order != null) {
            println("\nДетали заказа:")
            println("ID заказа: ${order.id}")
            println("ID клиента: ${order.customerId}")
            println("Номер столика: ${order.tableNumber}")
            println("Статус заказа: ${order.status}")
            println("\nБлюда:")
            order.items.forEach { item ->
                println("- ${item.dish} (x${item.quantity}) - ${item.price} руб. общая стоимость = ${item.subtotal} руб.")
            }
            println("\nВсего: ${order.totalAmount} руб.")
        } else {
            println("Заказ не найден!")
        }
    }

    private fun showAllOrders() {
        val orders = restaurantFacade.getAllOrders()

        if (orders.isEmpty()) {
            println("Нет доступных заказов.")
            return
        }

        println("\n=== Все заказы ===")
        println("%-36s %-15s %-15s %-15s %-15s".format(
            "ID", "Клиент", "Столик", "Статус", "Сумма"
        ))
        println("-".repeat(100))

        orders.forEach { order ->
            println("%-36s %-15s %-15d %-15s %,.2f руб.".format(
                order.id,
                order.customerId,
                order.tableNumber,
                order.status,
                order.totalAmount
            ))
        }
    }

    private fun showOrderDetails() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        val order = restaurantFacade.getOrder(orderId) ?: run {
            println("Заказ не найден")
            return
        }

        println("\n=== Информация о заказе ===")
        println("ID заказа: ${order.id}")
        println("ID клиента: ${order.customerId}")
        println("Номер столика: ${order.tableNumber}")
        println("Статус: ${order.status}")
        println("Общая сумма: %.2f руб.".format(order.totalAmount))
        println("Создан: ${order.createdAt.format(dateFormatter)}")
        println("Последнее обновление: ${order.updatedAt.format(dateFormatter)}")

        if (order.items.isNotEmpty()) {
            println("\nБлюда в заказе:")
            println(
                "| %-36s | %-15s | %-7s | %-10s | %-10s |".format(
                    "ID", "Блюдо", "Кол-во", "Цена", "Сумма"
                )
            )
            println("-".repeat(92))
            order.items.forEach { item ->
                println(
                    "| %-36s | %-15s | %-7d | %10.2f | %10.2f |".format(
                        item.id,
                        item.dish,
                        item.quantity,
                        item.price,
                        item.subtotal
                    )
                )
            }

        }
    }

    private fun addDishToOrder() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        print("Введите название блюда: ")
        val dish = scanner.nextLine().trim()

        print("Введите количество: ")
        val quantity = readIntInput()
        scanner.nextLine()

        print("Введите цену за единицу: ")
        val price = readDoubleInput()
        scanner.nextLine()

        try {
            restaurantFacade.addDishToOrder(orderId, dish, quantity, price)
            println("Блюдо успешно добавлено в заказ!")
        } catch (e: Exception) {
            println("Ошибка при добавлении блюда: ${e.message}")
        }
    }

    private fun removeDishFromOrder() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        print("Введите ID позиции заказа: ")
        val orderItemId = scanner.nextLine().trim()

        try {
            restaurantFacade.removeDishFromOrder(orderId, orderItemId)
            println("Блюдо успешно удалено из заказа!")
        } catch (e: Exception) {
            println("Ошибка при удалении блюда: ${e.message}")
        }
    }

    private fun updateOrderStatus() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        println("\nДоступные статусы:")
        OrderStatus.entries.forEach { status ->
            println("${status.ordinal}. $status")
        }
        print("Выберите новый статус (номер): ")
        val statusOrdinal = readIntInput()
        scanner.nextLine()

        try {
            val newStatus = OrderStatus.entries[statusOrdinal]
            restaurantFacade.updateOrderStatus(orderId, newStatus)
            println("Статус заказа успешно обновлен!")
        } catch (e: Exception) {
            println("Ошибка при обновлении статуса: ${e.message}")
        }
    }

    private fun cancelOrder() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        print("Введите причину отмены: ")
        val reason = scanner.nextLine().trim()

        try {
            restaurantFacade.cancelOrder(orderId, reason)
            println("Заказ успешно отменен!")
        } catch (e: Exception) {
            println("Ошибка при отмене заказа: ${e.message}")
        }
    }

    private fun showOrderStatistics() {
        val stats = restaurantFacade.getOrderStatistics()

        println("\n=== Статистика заказов ===")
        println("Всего заказов: ${stats.totalOrders}")
        println("Выполненных заказов: ${stats.completedOrders}")
        println("Общая выручка: %.2f руб.".format(stats.totalRevenue))
        println("Средняя сумма заказа: %.2f руб.".format(stats.averageOrderValue))
    }

    private fun readIntInput(): Int {
        return try {
            scanner.nextInt()
        } catch (e: Exception) {
            -1
        }
    }

    private fun readDoubleInput(): Double {
        return try {
            scanner.nextDouble()
        } catch (e: Exception) {
            -1.0
        }
    }
}
