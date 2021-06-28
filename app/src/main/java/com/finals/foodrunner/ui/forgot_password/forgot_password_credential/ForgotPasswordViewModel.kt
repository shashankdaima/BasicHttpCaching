package com.finals.foodrunner.ui.forgot_password.forgot_password_credential

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.JsonObjectRequest
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ForgotPasswordViewModel(
    private val volleySingleton: VolleySingleton, val connectivityManager: ConnectivityManager
) : ViewModel() {
    val mobileNumber = MutableLiveData<String>();
    val email = MutableLiveData<String>();
    private val channel= Channel<Event>()
    val eventChannel=channel.receiveAsFlow().asLiveData()

    fun saveMobile(str: String) {
        if (mobileNumber.value == str) {
            return;
        }
        this.mobileNumber.postValue(str)
    }

    fun saveEmail(str: String) {
        if (email.value == str) {
            return
        }
        this.email.postValue(str)
    }

    fun submitCredentials() {
        if(connectivityManager.isOnline()&&connectivityManager.checkConnectivity()){
            val jsonParams = JSONObject(
                mapOf(
                    "mobile_number" to mobileNumber.value.toString(),
                    "email" to email.value.toString()
                )
            )
            submitCreditentials(volleySingleton = volleySingleton,jsonParams = jsonParams,
                forgotPasswordResponseInterface = object:ForgotPasswordResponseInterface{
                    override fun onResponse(isFirstTime: Boolean) {
                        if(isFirstTime){
                            viewModelScope.launch { channel.send(Event.Success()) }

                        }
                        else{
                            viewModelScope.launch { channel.send(Event.SuccessAndSecondInvoke()) }

                        }
                    }

                    override fun onError(message: String) {
                        viewModelScope.launch { channel.send(Event.Error(message)) }

                    }

                })



        }else{
            viewModelScope.launch { channel.send(Event.NoInternet()) }


        }
    }
    sealed class Event{
        class Error(val message:String) : Event()
        class Success() : Event()
        class SuccessAndSecondInvoke: Event()
        class NoInternet: Event()

    }

}