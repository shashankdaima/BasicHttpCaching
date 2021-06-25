package com.finals.foodrunner.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class LoginViewModelFactory(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        LoginViewModel(volleySingleton, connectivityManager) as T
}