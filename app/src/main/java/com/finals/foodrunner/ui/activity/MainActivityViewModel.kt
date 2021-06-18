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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val api: RestaurantApi,
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    restaurantDatabase: RestaurantDatabase
) : ViewModel(), HomeViewModel, MyProfileViewModel, FavouriteRestaurant, OrderHistory {

    val restaurantDao = restaurantDatabase.restaurantDao()
    private val restaurantSortOrder = MutableLiveData(SORT_SCHEME.SORT_BY_RATING)
    private val query = MutableLiveData<String>();
    val connectionChannel = Channel<ConnectivityCheck>();
    val eventChannel = Channel<Events>()

    init {
        reloadHome()

    }

    override fun reloadHome() {
        updateAllRestaurants(forceRefresh = true)


    }


    //Home View Model
    override fun updateAllRestaurants(forceRefresh: Boolean) {

        if (connectivityManager.checkConnectivity() && connectivityManager.isOnline()) {

            viewModelScope.launch {
                eventChannel.send(Events.LoadingStart())
                connectionChannel.send(ConnectivityCheck.ONLINE)

            }


            api.getAllRestaurants(volleySingleton,object : RestaurantResponseInterface {
                override fun onError(throwable: Throwable, list: List<Restaurant>?) {
                    list?.let {
                        viewModelScope.launch { restaurantDao.addRestaurant(list) }

                    }
                    viewModelScope.launch {
                        eventChannel.send(Events.LoadingComplete())
                        eventChannel.send(Events.ErrorHomeFragment(throwable))
                    }
                }

                override fun onResponse(list: List<Restaurant>) {

                    viewModelScope.launch {
                        eventChannel.send(Events.LoadingComplete())
                        restaurantDao.addRestaurant(list)
                    }
                }


            })


        } else {
            viewModelScope.launch {
                eventChannel.send(Events.LoadingComplete())
                connectionChannel.send(ConnectivityCheck.OFFLINE)

            }
        }

    }

    override fun fetchFromDatabase() : Flow<List<Restaurant>>? {

        return restaurantSortOrder.value?.let { restaurantDao.getAllRestaurants("", sortBy = it) }
    }

    override fun getSortOrderLiveData(): MutableLiveData<SORT_SCHEME> {
        return this.restaurantSortOrder
    }


    override fun sortOrderChanged(index: Int) {

        when (index) {
            0 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_RATING)
            1 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_INC_COST)
            2 -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_DES_COST)
            else -> this.restaurantSortOrder.postValue(SORT_SCHEME.SORT_BY_RATING)

        }


    }

    override fun setQuery(query: String) {
        this.query.postValue(query)
    }

    override fun getQuery(): LiveData<String> {
        return query
    }

    override fun getHomeEventChannel() = eventChannel


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
        class LoadingStart : Events()
        class LoadingComplete : Events()
    }
}