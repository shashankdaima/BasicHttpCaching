package com.finals.foodrunner.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.User
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.UserAuthResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SignUpViewModel(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager
) : ViewModel() {
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>();
    val mobileNumber = MutableLiveData<String>();
    val location = MutableLiveData<String>();
    val password = MutableLiveData<String>();
    val confirmPassword = MutableLiveData<String>();
    val eventChannel = Channel<Event>()
    fun nameChanged(name: String) {
        this.name.postValue(name)
    }

    fun emailChanged(email: String) {
        this.email.postValue(email)
    }

    fun mobileNumberChange(mobileNumber: String) {
        this.mobileNumber.postValue(mobileNumber)
    }

    fun locationChanged(location: String) {
        this.location.postValue(location)
    }

    fun passwordChanged(password: String) {
        this.password.postValue(password)
    }

    fun confirmPasswordChanged(confirmPassword: String) {
        this.confirmPassword.postValue(confirmPassword)
    }

    fun signUp() {
        if (confirmPassword.value != password.value) {
            viewModelScope.launch { eventChannel.send(Event.PasswordMismatch) }
            return
        }
        com.finals.foodrunner.volley.signUp(
            name = name.value.toString(),
            email = email.value.toString(),
            mobileNumber = mobileNumber.value.toString(),
            address = location.value.toString(),
            password = password.value.toString(),
            volleySingleton = volleySingleton,
            onUserAuthResponseInterface = object : UserAuthResponseInterface {
                override fun onError(message: String) {
                    viewModelScope.launch { eventChannel.send(Event.Error(message)) }

                }

                override fun onResponse(user: User) {
                    viewModelScope.launch { eventChannel.send(Event.Success(user)) }

                }
            }
        )
    }

    sealed class Event {
        class Success(val user: User) : Event()
        object PasswordMismatch : Event()
        class Error(val message: String) : Event()


    }


}