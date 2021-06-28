package com.finals.foodrunner.objects

data class OrderHistoryElement(
    val food_items: List<FoodItem>,
    val order_id: String,
    val order_placed_at: String,
    val restaurant_name: String,
    val total_cost: String
)