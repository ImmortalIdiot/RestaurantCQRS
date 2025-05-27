package com.immortalidiot.command.repository

import com.immortalidiot.command.model.Dish
import com.immortalidiot.command.model.DishCategory

object DishRepository {
    private val dishes = listOf(
        Dish("Суп сырный", DishCategory.SOUPS, 450.0),
        Dish("Суп Том Ям", DishCategory.SOUPS, 550.0),
        Dish("Борщ", DishCategory.SOUPS, 350.0),

        Dish("Салат Цезарь", DishCategory.SALADS, 350.0),
        Dish("Салат Греческий", DishCategory.SALADS, 250.0),
        Dish("Салат Оливье", DishCategory.SALADS, 300.0),

        Dish("Кока-кола", DishCategory.DRINKS, 80.0),
        Dish("Фанта", DishCategory.DRINKS, 80.0),
        Dish("Вода", DishCategory.DRINKS, 45.0),
        Dish("Лимонад \"Дюшес\"", DishCategory.DRINKS, 60.99),
        Dish("Милкшейк \"Клубника\"", DishCategory.DRINKS, 120.0),
        Dish("Милкшейк \"Ваниль\"", DishCategory.DRINKS, 110.99),

        Dish("Торт фирменный", DishCategory.DESSERTS, 1400.0),
        Dish("Чизкейк", DishCategory.DESSERTS, 199.50),
        Dish("Зимняя вишня", DishCategory.DESSERTS, 230.99),

        Dish("Стейк из индейки", DishCategory.MEAT, 450.0),
        Dish("Стейк свиной барбекю", DishCategory.MEAT, 550.0),
        Dish("Стейк говяжий острый со специями", DishCategory.MEAT, 650.0)

    )


    fun getAllDishes(): List<Dish> = dishes

    fun getDishByName(name: String): Dish? = dishes.find { it.name.equals(name, ignoreCase = true) }

    fun getDishByNumberOrName(input: String): Dish? {
        val sortedDishes = dishes.sortedWith(compareBy({ it.category }, { it.name }))
        val number = input.toIntOrNull()
        return if (number != null) {
            if (number in 1..sortedDishes.size) sortedDishes[number - 1] else null
        } else {
            dishes.find { it.name.equals(input, ignoreCase = true) }
        }
    }

    fun printMenu() {
        var counter = 1
        var currentCategory: DishCategory? = null

        val sortedDishes = dishes.sortedWith(compareBy({ it.category }, { it.name }))

        for (dish in sortedDishes) {
            if (dish.category != currentCategory) {
                currentCategory = dish.category
                println("\n${currentCategory.displayName}:")
            }
            println("  $counter. ${dish.name} — ${"%,.2f".format(dish.price)} руб.")
            counter++
        }
    }
}
