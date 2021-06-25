package com.finals.foodrunner.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.finals.foodrunner.objects.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Restaurant::class],version = 1)
abstract class RestaurantDatabase:RoomDatabase() {

    abstract fun restaurantDao():RestaurantDao

}