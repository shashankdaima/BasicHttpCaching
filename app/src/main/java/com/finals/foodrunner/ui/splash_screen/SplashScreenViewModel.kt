package com.finals.foodrunner.ui.splash_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finals.foodrunner.util.ConnectivityManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SplashScreenViewModel(
) : ViewModel() {

     var isLoaded=false
    private val _channel = Channel<NetworkRequestResult>()
    val splashScreenChannel = _channel.receiveAsFlow()
    suspend fun checkInternetAvailability(connectivityManager: ConnectivityManager) {
        if(connectivityManager.isOnline()){
            _channel.send(NetworkRequestResult.SUCCESS)
        }
        else{
            _channel.send(NetworkRequestResult.FAILURE)
        }
    }
    fun setIsLoaded(boolean: Boolean){
        this.isLoaded=boolean
    }

    enum class NetworkRequestResult {
        SUCCESS, FAILURE
    }
}
