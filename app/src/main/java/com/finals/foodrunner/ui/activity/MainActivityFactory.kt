package com.finals.foodrunner.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.RestaurantApi
import com.finals.foodrunner.volley.VolleySingleton

class MainActivityFactory(
    val api: RestaurantApi,
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager,
    val restaurantDatabase: RestaurantDatabase
) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(api,volleySingleton,connectivityManager,restaurantDatabase) as T
        }
}