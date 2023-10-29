package com.saeeed.devejump.project.tailoring.presentation.ui.list

enum class Category(val value: String){
    TAILORING("خیاطی"),
    KNITTING("بافتنی"),
    LEATHERING("چرم دوزی"),
    EMBROIDERY("گل دوزی"),
    TERMEHDOOZY("ترمه دوزی"),
    DOLL_MAKING("عروسک دوزی"),
    OTHER("سایر"),

}

fun getAllCategories(): List<Category>{
    return listOf(
        Category.TAILORING, Category.KNITTING, Category.LEATHERING, Category.EMBROIDERY,
        Category.TERMEHDOOZY, Category.DOLL_MAKING, Category.OTHER
    )
}

fun getCategory(value: String): Category? {
    val map = Category.values().associateBy(Category::value)
    return map[value]
}