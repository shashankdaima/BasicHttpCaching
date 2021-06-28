package com.finals.foodrunner.ui.forgot_password.password_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class PasswordSaveViewModelFactory(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        PasswordSaveViewModel(volleySingleton, connectivityManager) as T
}