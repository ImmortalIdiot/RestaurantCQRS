package com.immortalidiot.api.ui

import com.immortalidiot.api.facade.RestaurantFacade
import com.immortalidiot.command.model.DishCategory
import com.immortalidiot.command.repository.DishRepository
import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.common.exception.OrderExceptions
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
        print("Введите ID клиента: ")
        val customerId = readlnOrNull()?.trim()
        if (customerId.isNullOrBlank()) {
            println("Некорректный ID клиента.")
            return
        }

        print("Введите номер столика: ")
        val tableNumber = readlnOrNull()?.toIntOrNull()
        if (tableNumber == null || tableNumber <= 0) {
            println("Некорректный номер столика.")
            return
        }

        try {
            restaurantFacade.createOrder(customerId, tableNumber)
            val orderId = restaurantFacade.getAllOrders().last().id

            while (true) {
                print("\nДобавить блюдо в заказ? (Да/Нет): ")
                when (readlnOrNull()?.trim()?.lowercase()) {
                    "да" -> {
                        DishRepository.printMenu()

                        print("Введите название блюда или номер позиции: ")
                        val input = readlnOrNull()?.trim()
                        val dish = input?.let { DishRepository.getDishByNumberOrName(it) }
                        if (dish == null) {
                            println("Ошибка: блюдо \"$input\" не найдено в меню.")
                            continue
                        }

                        print("Введите количество: ")
                        val quantity = readlnOrNull()?.toIntOrNull()
                        if (quantity == null || quantity <= 0) {
                            println("Некорректное количество.")
                            continue
                        }

                        try {
                            restaurantFacade.addDishToOrder(orderId, dish, quantity)
                            println(
                                "Блюдо \"${dish.name}\" успешно добавлено в заказ: $quantity шт. на сумму ${
                                    "%,.2f".format(dish.price * quantity)
                                } руб."
                            )
                        } catch (e: Exception) {
                            println("Ошибка при добавлении блюда: ${e.message}")
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
        println(
            "%-36s %-15s %-15s %-15s %-15s".format(
                "ID", "Клиент", "Столик", "Статус", "Сумма"
            )
        )
        println("-".repeat(100))

        orders.forEach { order ->
            println(
                "%-36s %-15s %-15d %-15s %,.2f руб.".format(
                    order.id,
                    order.customerId,
                    order.tableNumber,
                    order.status,
                    order.totalAmount
                )
            )
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
                "| %-36s | %-20s | %-7s | %-10s | %-10s |".format(
                    "ID", "Блюдо", "Кол-во", "Цена", "Сумма"
                )
            )
            println("-".repeat(92))
            order.items.forEach { item ->
                println(
                    "| %-36s | %-20s | %-7d | %10.2f | %10.2f |".format(
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

        DishRepository.printMenu()

        print("Введите название блюда или номер позиции: ")
        val input = scanner.nextLine().trim()
        val dish = DishRepository.getDishByNumberOrName(input)
        if (dish == null) {
            println("Ошибка: блюдо \"$input\" не найдено в меню.")
            return
        }

        print("Введите количество: ")
        val quantity = readIntInput()
        scanner.nextLine()

        try {
            restaurantFacade.addDishToOrder(orderId, dish, quantity)
            println("Блюдо успешно добавлено в заказ!")
        } catch (e: Exception) {
            println("Ошибка при добавлении блюда: ${e.message}")
        }
    }

    private fun removeDishFromOrder() {
        print("Введите ID заказа: ")
        val orderId = scanner.nextLine().trim()

        val order = restaurantFacade.getOrder(orderId)
        if (order == null) {
            println("Заказ не найден")
            return
        }

        if (order.items.isEmpty()) {
            println("В заказе нет блюд.")
            return
        }

        println("Блюда в заказе:")
        println("№  | Название             | Количество")
        println("---|----------------------|----------")

        order.items.forEachIndexed { index, item ->
            println("${index + 1}. | ${item.dish.padEnd(20)} | ${item.quantity}")
        }

        print("Введите номер блюда для удаления: ")
        val dishNumber = scanner.nextLine().toIntOrNull()
        if (dishNumber == null || dishNumber !in 1..order.items.size) {
            println("Неверный номер блюда.")
            return
        }

        val selectedItem = order.items[dishNumber - 1]
        val quantity: Int

        if (selectedItem.quantity == 1) {
            quantity = 1
        } else {
            println("Введите количество для удаления (максимум ${selectedItem.quantity}): ")
            val quantityToRemove = readIntInput()
            scanner.nextLine()

            if (quantityToRemove <= 0 || quantityToRemove > selectedItem.quantity) {
                println("Неверное количество.")
                return
            }
            quantity = quantityToRemove
        }

        try {
            val dishObj = DishRepository.getDishByName(selectedItem.dish)
            if (dishObj == null) {
                println("Ошибка: блюдо не найдено в репозитории.")
                return
            }

            restaurantFacade.removeDishFromOrder(orderId, dishObj, quantity)
            println("Удалено $quantity шт. блюда \"${selectedItem.dish}\" из заказа.")
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
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
