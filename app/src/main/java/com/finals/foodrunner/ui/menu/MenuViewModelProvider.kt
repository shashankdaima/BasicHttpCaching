package com.finals.foodrunner.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class MenuViewModelProvider(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager,
    val restaurants: Restaurant,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(volleySingleton,connectivityManager,restaurants) as T
    }
}