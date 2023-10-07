package com.saeeed.devejump.project.tailoring.presentation.ui.list

enum class Category(val value: String){
    CHICKEN("Chicken"),
    BEEF("Beef"),
    SOUP("Soup"),
    DESSERT("Dessert"),
    VEGETARIAN("Vegetarian"),
    MILK("Milk"),
    VEGAN("Vegan"),
    PIZZA("Pizza"),
    DONUT("Donut"),
}

fun getAllCategories(): List<Category>{
    return listOf(
        Category.CHICKEN, Category.BEEF, Category.SOUP, Category.DESSERT,
        Category.VEGETARIAN, Category.MILK, Category.VEGAN, Category.PIZZA, Category.DONUT
    )
}

fun getCategory(value: String): Category? {
    val map = Category.values().associateBy(Category::value)
    return map[value]
}