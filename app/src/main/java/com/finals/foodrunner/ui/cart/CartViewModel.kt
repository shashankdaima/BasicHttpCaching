package com.finals.foodrunner.ui.cart

import androidx.lifecycle.ViewModel
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton
import kotlinx.coroutines.channels.Channel

class CartViewModel(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager,
    val orderedFood:Array<MenuItem>
): ViewModel() {
    init {

    }

    val eventChannel= Channel<Event>()
    enum class Event{
        LOADING,SUCCESS,FAILURE,OFFLINE
    }
}