package com.finals.foodrunner.ui.forgot_password.forgot_password_credential

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class ForgotPasswordViewModelFactory(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ForgotPasswordViewModel(volleySingleton, connectivityManager) as T
}