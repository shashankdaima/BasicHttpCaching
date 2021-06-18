package com.finals.foodrunner.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finals.foodrunner.objects.Restaurant

@Database(entities = [Restaurant::class],version = 1)
abstract class RestaurantDatabase:RoomDatabase() {

    abstract fun restaurantDao():RestaurantDao
}