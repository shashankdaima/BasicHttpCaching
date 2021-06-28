package com.finals.foodrunner.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class ConfirmationViewModelFactory(
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    private val orderedFood: Array<MenuItem>,
    val userId: Long
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConfirmationViewModel(volleySingleton,connectivityManager, orderedFood,userId) as T
    }

}