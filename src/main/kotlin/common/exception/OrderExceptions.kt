package com.immortalidiot.common.exception

object OrderExceptions {
    class InvalidQuantityException(quantity: Int) :
        IllegalArgumentException("Количество должно быть положительным, получено: $quantity")

    class InvalidPriceException(price: Double) :
        IllegalArgumentException("Цена должна быть положительной, получено: $price")

    class OrderNotFoundException(orderId: String) :
        RuntimeException("Заказ с ID $orderId не найден")

    class InvalidOrderStatusTransitionException(
        currentStatus: String,
        newStatus: String,
        validTransitions: Set<String>
    ) : IllegalStateException("Нельзя изменить статус с $currentStatus на $newStatus. Допустимые переходы: $validTransitions")

    class DishNotFoundException(dish: String) :
        RuntimeException("Блюдо с названием $dish не найдено")

    class OrderItemNotFoundException(itemId: String) :
        RuntimeException("Позиция заказа с ID $itemId не найдена")

    class InvalidTableNumberException(tableNumber: Int) :
        IllegalArgumentException("Номер стола должен быть положительным, получено: $tableNumber")

    class BlankOrderIdException :
        IllegalArgumentException("ID заказа не может быть пустым")

    class BlankCustomerIdException :
        IllegalArgumentException("ID клиента не может быть пустым")

    class BlankDishException :
        IllegalArgumentException("Название блюда не может быть пустым")

    class BlankCancellationReasonException :
        IllegalArgumentException("Причина отмены не может быть пустой")

    class OrderNotModifiableException(status: String) :
        IllegalStateException("Нельзя изменить заказ со статусом: $status. Заказ должен быть в статусе CREATED.")
}

