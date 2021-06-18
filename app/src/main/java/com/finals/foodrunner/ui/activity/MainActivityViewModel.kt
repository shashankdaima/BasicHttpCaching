package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.room.SORT_SCHEME
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.RestaurantApi
import com.finals.foodrunner.volley.RestaurantResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val api: RestaurantApi,
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    restaurantDatabase: RestaurantDatabase
) : ViewModel(), HomeViewModel, MyProfileViewModel, FavouriteRestaurant, OrderHistory {
    private val _allRestaurants = MutableLiveData<List<Restaurant>>();
    private val allRestaurants: LiveData<List<Restaurant>> = _allRestaurants
    val connectionChannel = Channel<ConnectivityCheck>();
    val eventChannel = Channel<Events>()
    val restaurantDao = restaurantDatabase.restaurantDao()
    private val restaurantSortOrder = MutableLiveData<SORT_SCHEME>(SORT_SCHEME.SORT_BY_RATING)


    init {
        reload()

    }

    fun reload() {
        updateAllRestaurants()


    }


    //Home View Model
    override fun updateAllRestaurants(forceRefresh: Boolean) {

        if (connectivityManager.checkConnectivity() && connectivityManager.isOnline()) {
            viewModelScope.launch {

                connectionChannel.send(ConnectivityCheck.ONLINE)
            }

            api.getAllRestaurants(volleySingleton, object : RestaurantResponseInterface {
                override fun onError(throwable: Throwable, list: List<Restaurant>?) {
                    list?.let {
                        viewModelScope.launch { restaurantDao.addRestaurant(list) }

                    }
                    viewModelScope.launch {

                        eventChannel.send(Events.ErrorHomeFragment(throwable))
                    }
                }

                override fun onResponse(list: List<Restaurant>) {

                    viewModelScope.launch { restaurantDao.addRestaurant(list) }
                }


            })


        } else {
            viewModelScope.launch {

                connectionChannel.send(ConnectivityCheck.OFFLINE)
            }
        }

    }

    override fun fetchFromDatabase() {
        viewModelScope.launch {
            restaurantSortOrder.value?.let {
                restaurantDao.getAllRestaurants("", it).collect {
                    _allRestaurants.postValue(it)
                }
            }
        }


    }

    override fun getSortOrderLiveData(): MutableLiveData<SORT_SCHEME> {
        return this.restaurantSortOrder
    }

    override fun fetchAllRestaurants() =
        this.allRestaurants

    override fun sortOrderChanged(index: Int) {

        when (index) {
            0 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_RATING)
            1 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_INC_COST)
            2 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_DES_COST)
            else -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_RATING)

        }

    }


    override fun restaurantFavChanged(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavourite = !restaurant.isFavourite
            restaurantDao.updateRestaurant(restaurant)
        }
    }

    enum class ConnectivityCheck {
        ONLINE, OFFLINE
    }

    sealed class Events {
        data class ErrorHomeFragment(val error: Throwable) : Events()
    }
}