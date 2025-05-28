package com.immortalidiot

import com.immortalidiot.api.facade.RestaurantFacade
import com.immortalidiot.api.ui.ConsoleInterface
import com.immortalidiot.command.command.*
import com.immortalidiot.command.handler.*
import com.immortalidiot.command.repository.DishRepository
import com.immortalidiot.command.repository.InMemoryOrderRepository
import com.immortalidiot.common.event.EventBus
import com.immortalidiot.common.event.OrderStatus
import com.immortalidiot.query.repository.OrderViewRepository
import com.immortalidiot.query.service.OrderEventHandler
import com.immortalidiot.query.service.OrderQueryService

fun main() {
    val commandOrderRepository = InMemoryOrderRepository()
    val queryOrderRepository = OrderViewRepository()
    val dishRepository = DishRepository

    val eventHandler = OrderEventHandler(queryOrderRepository)
    EventBus.getInstance().register(eventHandler)

    val commandBus = CommandBus().apply {
        register(CreateOrderCommand::class.java, CreateOrderCommandHandler(commandOrderRepository))
        register(AddDishToOrderCommand::class.java, AddDishCommandHandler(commandOrderRepository))
        register(RemoveDishFromOrderCommand::class.java, RemoveDishCommandHandler(commandOrderRepository))
        register(UpdateOrderStatusCommand::class.java, UpdateOrderStatusCommandHandler(commandOrderRepository))
        register(CancelOrderCommand::class.java, CancelOrderCommandHandler(commandOrderRepository))
    }

    val queryService = OrderQueryService(queryOrderRepository)

    val restaurantFacade = RestaurantFacade(commandBus, queryService)

    try {
        println("Создание тестовых данных...")

        restaurantFacade.createOrder("Клиент1", 1)
        restaurantFacade.createOrder("Клиент2", 2)

        val orders = restaurantFacade.getAllOrders()
        val order1Id = orders[0].id
        val order2Id = orders[1].id

        restaurantFacade.addDishToOrder(order1Id, dishRepository.getDishByName("Суп сырный")!!, 2)
        restaurantFacade.addDishToOrder(order1Id, dishRepository.getDishByName("Стейк из индейки")!!, 1)
        restaurantFacade.addDishToOrder(order1Id, dishRepository.getDishByName("Кока-кола")!!, 2)

        restaurantFacade.addDishToOrder(order2Id, dishRepository.getDishByName("Салат Цезарь")!!, 1)
        restaurantFacade.addDishToOrder(order2Id, dishRepository.getDishByName("Торт фирменный")!!, 2)
        restaurantFacade.addDishToOrder(order2Id, dishRepository.getDishByName("Милкшейк \"Клубника\"")!!, 1)

        restaurantFacade.updateOrderStatus(order2Id, OrderStatus.IN_PROGRESS)
        restaurantFacade.updateOrderStatus(order2Id, OrderStatus.READY)
        restaurantFacade.updateOrderStatus(order2Id, OrderStatus.COMPLETED)

        println("Тестовые данные созданы")
        println("Созданы заказы с id: $order1Id, $order2Id")

        println("\nСостояние текущего заказа:")
        restaurantFacade.getAllOrders().forEach { order ->
            println("Заказ ${order.id}:")
            println("  Заказчик: ${order.customerId}")
            println("  Номер стола: ${order.tableNumber}")
            println("  Статус: ${OrderStatus.fromString(order.status).displayName}")
            println("  Счёт: ${"%.2f".format(order.totalAmount)} руб.")
            println("  Количество блюд: ${order.items.size}")
            println()
        }

    } catch (e: Exception) {
        println("Ошибка создания тестовых данных: ${e.message}")
        e.printStackTrace()
        return
    }

    println("\nЗапуск консольного интерфейса...")
    val consoleUI = ConsoleInterface(restaurantFacade)
    consoleUI.start()
}