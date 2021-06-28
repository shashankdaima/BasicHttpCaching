package com.finals.foodrunner.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class SignUpViewModelFactory(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager
) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(volleySingleton,connectivityManager) as T
    }
}