package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.SORT_SCHEME
import com.finals.foodrunner.volley.RestaurantResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.getAllRestaurants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    restaurantDatabase: RestaurantDatabase,
) : ViewModel(), HomeViewModel,FavouriteViewModel {
    private val homeEventChannel = Channel<Event>()
    val homeEvents = homeEventChannel.receiveAsFlow().asLiveData()
    val restaurantDao = restaurantDatabase.restaurantDao()
    val allRestaurant = restaurantDao.getAllRestaurants("", SORT_SCHEME.SORT_BY_RATING).asLiveData()
    val favouriteRestaurants = restaurantDao.getFavouriteRestaurants().asLiveData()

    init {
        viewModelScope.launch {
            restaurantDao.deleteAllRestaurants()
            fetchAllRestaurants()
        }
    }

    override suspend fun fetchAllRestaurants() {
        if (connectivityManager.isOnline() && connectivityManager.checkConnectivity()) {
            viewModelScope.launch {
                homeEventChannel.send(Event.LOADING)
            }
            getAllRestaurants(volleySingleton, object : RestaurantResponseInterface {
                override fun onError(throwable: Throwable, list: List<Restaurant>?) {
                    viewModelScope.launch {
                        homeEventChannel.send(Event.LOADED)
                    }
                }

                override fun onResponse(list: List<Restaurant>) {
                    CoroutineScope(SupervisorJob()).launch {
                        restaurantDao.addRestaurant(list)
                        homeEventChannel.send(Event.LOADED)

                    }
                }

            })
        } else {
            viewModelScope.launch {
                homeEventChannel.send(Event.OFFLINE)
            }

        }
    }

    override fun getRestaurants(): LiveData<List<Restaurant>> =allRestaurant

    override fun changeFavouriteStatus(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavourite = !restaurant.isFavourite
            restaurantDao.updateRestaurant(restaurant)
        }
    }

    enum class Event {
        LOADED, LOADING, OFFLINE
    }

    override fun getFavRestaurants(): LiveData<List<Restaurant>> = favouriteRestaurants

    override fun unFavStatus(restaurant: Restaurant) {
        changeFavouriteStatus(restaurant)
    }
}