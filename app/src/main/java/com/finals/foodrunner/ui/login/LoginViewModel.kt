package com.finals.foodrunner.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.User
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.UserAuthResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val volleySingleton: VolleySingleton, val connectivityManager: ConnectivityManager
) : ViewModel() {
    val mobileLogin = MutableLiveData<String>();
    val passwordLogin = MutableLiveData<String>();
    private val channel= Channel<Event>()
    val eventChannel=channel.receiveAsFlow().asLiveData()

    fun saveMobile(str: String) {
        if (mobileLogin.value == str) {
            return;
        }
        this.mobileLogin.postValue(str)
    }

    fun savePassword(str: String) {
        if (passwordLogin.value == str) {
            return
        }
        this.passwordLogin.postValue(str)
    }

    fun login() {
        com.finals.foodrunner.volley.login(
            mobileNumber = this.mobileLogin.value.toString(),
            password = this.passwordLogin.value.toString(),
            volleySingleton,
            object : UserAuthResponseInterface {
                override fun onError(message: String) {
                    viewModelScope.launch{ channel.send(Event.Error(message)) }
                }
                override fun onResponse(user: User) {
                    viewModelScope.launch { channel.send(Event.Success(user)) }
                }

            })

    }
    sealed class Event{
        class Error(val message:String) :Event()
        class Success(val user: User) :Event()

    }

}