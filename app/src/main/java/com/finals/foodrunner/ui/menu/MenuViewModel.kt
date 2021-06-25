package com.finals.foodrunner.ui.menu

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.MenuResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.getMenu
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MenuViewModel(
    var volleySingleton: VolleySingleton,
    var connectivityManager: ConnectivityManager,
    val restaurants: Restaurant
) : ViewModel() {
    val eventChannel = Channel<Event>()
    val menu = MutableLiveData<List<MenuItem>>();

    init {
        viewModelScope.launch { getMenu(restaurants) }
    }

    suspend fun getMenu(restaurant: Restaurant) {

        if (connectivityManager.checkConnectivity() && connectivityManager.isOnline()) {
            eventChannel.send(Event.LoadingStart())
            Handler().postDelayed({
                viewModelScope.launch {
                    eventChannel.send(Event.LoadingStart())
                }
                getMenu(volleySingleton, restaurant, object : MenuResponseInterface {
                    override fun onError(message: String) {
                        viewModelScope.launch {

                            eventChannel.send(Event.LoadingComplete())
                            eventChannel.send(Event.Error(message))

                        }
                    }

                    override fun onResponse(list: List<MenuItem>) {
                        viewModelScope.launch {
                            eventChannel.send(Event.LoadingComplete())
                        }
                        menu.postValue(list)

                    }

                })
            }, 200)
        } else {
            viewModelScope.launch {

                eventChannel.send(Event.Offline())
            }
        }


    }

    suspend fun updateSelectionStatus(menuItem: MenuItem) {
        val list = menu.value?.toMutableList()
        val index = list?.indexOf(menuItem)
        list?.remove(menuItem)
        menuItem.isOrder = !menuItem.isOrder;
        list?.add(index!!, menuItem)
        menu.postValue(list!!)
    }

    fun getOrderedFood():Array<MenuItem>{
        val menu=menu.value
        val result=ArrayList<MenuItem>();
        if (menu != null) {
            for(i in menu){
                if(i.isOrder){
                    result.add(i)
                }
            }
        }
        return result.toTypedArray()
    }

    sealed class Event {
        class LoadingStart : Event()
        class LoadingComplete : Event()
        class Offline : Event()
        class Error(message: String) : Event()
    }


}