package com.projectmalthorn.dishme.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY : String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    fun dishTypes():ArrayList<String>{
        val list: ArrayList<String> = ArrayList<String>()
        list.add("Breakfast")
        list.add("Lunch")
        list.add("Dinner")
        list.add("Snacks")
        list.add("Salad")
        list.add("Side Dish")
        list.add("Dessert")
        list.add("Other")
        return list
    }

    fun dishCategories():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Pizza")
        list.add("BBQ")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Sandwich")
        list.add("Wraps")
        list.add("Other")
        return list
    }

    fun dishCookTime():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("10")
        list.add("20")
        list.add("30")
        list.add("40")
        list.add("50")
        list.add("60")
        list.add("70")
        list.add("80")
        list.add("90")
        list.add("100")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }
}