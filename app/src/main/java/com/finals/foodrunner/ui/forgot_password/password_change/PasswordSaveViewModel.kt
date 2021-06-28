package com.finals.foodrunner.ui.forgot_password.password_change

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.OtpVerificationAndPasswordSubmission
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.otpVerifyAndSubmitPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class PasswordSaveViewModel(
    private val volleySingleton: VolleySingleton, val connectivityManager: ConnectivityManager
) : ViewModel() {
    val password = MutableLiveData<String>();
    val confirm_password = MutableLiveData<String>();
    val otp = MutableLiveData<String>()
    private val channel = Channel<Event>()
    val eventChannel = channel.receiveAsFlow().asLiveData()

    fun savePassword(str: String) {
        if (password.value == str) {
            return;
        }
        this.password.postValue(str)
    }

    fun saveConfirmPassword(str: String) {
        if (confirm_password.value == str) {
            return
        }
        this.confirm_password.postValue(str)
    }
    fun saveOtp(str: String) {
        if (otp.value == str) {
            return
        }
        this.otp.postValue(str)
    }

    fun verifyAndChangePassword(mobileNumber: String) {
        if(connectivityManager.isOnline()&&connectivityManager.checkConnectivity()){
            val params = JSONObject(
                mapOf(
                    "mobile_number" to mobileNumber,
                    "password" to password.value,
                    "otp" to otp.value
                )
            )
            otpVerifyAndSubmitPassword(
                volleySingleton,
                params,
                object : OtpVerificationAndPasswordSubmission {
                    override fun onResponse(message: String) {
                        viewModelScope.launch { channel.send(Event.Success(message)) }
                    }

                    override fun onError(message: String) {
                        viewModelScope.launch { channel.send(Event.Error(message)) }

                    }

                })
        }
        else{
            viewModelScope.launch{ channel.send(Event.NoInternet) }

        }

    }

    sealed class Event {
        class Error(val message: String) : Event()
        class Success(val message: String) : Event()
        object NoInternet : Event()


    }

}