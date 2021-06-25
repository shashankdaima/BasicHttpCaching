package com.finals.foodrunner.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.getAllRestaurants

class CartViewModelFactory(
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    private val orderedFood:Array<MenuItem>
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CartViewModel(volleySingleton,connectivityManager, orderedFood) as T
    }

}