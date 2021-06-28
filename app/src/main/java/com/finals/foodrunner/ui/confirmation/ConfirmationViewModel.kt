package com.finals.foodrunner.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.OrderResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.order
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    val volleySingleton: VolleySingleton,
    val connectivityManager: ConnectivityManager,
    private val orderedFood: Array<MenuItem>,
    private val userId: Long
) : ViewModel() {
    private val eventChannel= Channel<Event>()
    val events=eventChannel.receiveAsFlow().asLiveData()

    init {
        viewModelScope.launch {
            orderFood()
        }
    }

    suspend fun orderFood() {
        if (connectivityManager.checkConnectivity() && connectivityManager.isOnline()) {
            eventChannel.send(Event.LOADING)
            var totalCost = 0;
            val foods = arrayOfNulls<Int>(orderedFood.size);
            for ((index, i) in orderedFood.withIndex()) {
                totalCost += i.price
                foods[index] = i.sno
            }
            order(
                userId,
                orderedFood[0].restaurantId,
                totalCost,
                foods,
                volleySingleton,
                object : OrderResponseInterface {
                    override fun onResponse() {
                        viewModelScope.launch { eventChannel.send(Event.SUCCESS) }
                    }

                    override fun onError(message: String) {
                        viewModelScope.launch { eventChannel.send(Event.FAILURE) }
                    }

                })

        } else {
            eventChannel.send(Event.OFFLINE)
        }
    }


    enum class Event {
        LOADING, SUCCESS, FAILURE, OFFLINE
    }
}