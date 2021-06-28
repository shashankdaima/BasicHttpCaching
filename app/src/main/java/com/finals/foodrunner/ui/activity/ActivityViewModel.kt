package com.finals.foodrunner.ui.activity

import androidx.lifecycle.*
import com.finals.foodrunner.objects.OrderHistoryElement
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.objects.User
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.SORT_SCHEME
import com.finals.foodrunner.volley.OrderHistoryResponseInterface
import com.finals.foodrunner.volley.RestaurantResponseInterface
import com.finals.foodrunner.volley.VolleySingleton
import com.finals.foodrunner.volley.getAllRestaurants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val volleySingleton: VolleySingleton,
    private val connectivityManager: ConnectivityManager,
    restaurantDatabase: RestaurantDatabase,
) : ViewModel(), HomeViewModel, FavouriteViewModel, OrderHistoryViewModel, ProfileViewModel {
    private val homeEventChannel = Channel<Event>()
    val restaurantDao = restaurantDatabase.restaurantDao()
    private val searchQueryHome = MutableStateFlow("")
    private val sortOrderHome = MutableStateFlow(SORT_SCHEME.SORT_BY_RATING)
    private val allRestaurantFlow =
        combine(searchQueryHome, sortOrderHome) { searchQuery, sortOrder ->
            Pair(searchQuery, sortOrder)
        }.flatMapLatest { (query, sortOrder) ->
            restaurantDao.getAllRestaurants(query, sortOrder)
        }
    private val allRestaurant = allRestaurantFlow.asLiveData()

    private val searchQueryFav = MutableStateFlow("")
    private val sortOrderFav = MutableStateFlow(SORT_SCHEME.SORT_BY_RATING)
    private val favouriteRestaurantFlow =
        combine(searchQueryFav, sortOrderFav) { searchQueryFav, sortOrder ->
            Pair(searchQueryFav, sortOrder)
        }.flatMapLatest {(query,sortOrder)->
            restaurantDao.getFavRestaurants(query,sortOrder)
        }
    private val favouriteRestaurants = favouriteRestaurantFlow.asLiveData()
    val orderHistory = MutableLiveData<List<OrderHistoryElement>>()
    val orderHistoryEvent = Channel<Event>()
    private val currentUser = MutableLiveData<User>()
    val user: LiveData<User> = currentUser


//    ------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            restaurantDao.deleteUnFavouriteRestaurants()
            fetchAllRestaurants()
            fetchOrderHistory()
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

    override suspend fun setCurrentUser(user: User) {
        this.currentUser.postValue(user)
    }

    override fun setHomeSearchQuery(query: String) {
        this.searchQueryHome.value = query
    }

    override suspend fun currentUser() = user
    override fun setFavSortOrder(sortScheme: SORT_SCHEME) {
        this.sortOrderFav.value=sortScheme
    }

    override fun getFavSortOrder()=this.sortOrderFav.value
    override fun setFavSearchQuery(query: String) {
        searchQueryFav.value=query
    }

    override fun getRestaurants(): LiveData<List<Restaurant>> = allRestaurant
    override fun getHomeEvents(): Flow<Event> = homeEventChannel.receiveAsFlow()
    override fun setHomeSortOrder(sortScheme: SORT_SCHEME) {
        sortOrderHome.value = sortScheme
    }

    override fun getHomeSortOrder() = sortOrderHome.value

    override fun changeFavouriteStatus(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavourite = !restaurant.isFavourite
            restaurantDao.updateRestaurant(restaurant)
        }
    }


    override fun getFavRestaurants(): LiveData<List<Restaurant>> = favouriteRestaurants

    override fun unFavStatus(restaurant: Restaurant) {
        changeFavouriteStatus(restaurant)
    }


    override suspend fun fetchOrderHistory() {
        if (connectivityManager.isOnline() && connectivityManager.checkConnectivity()) {
            orderHistoryEvent.send(Event.LOADING)
            com.finals.foodrunner.volley.getOrderHistory(
                userId = user.value?.user_id.toString(),
                volleySingleton = volleySingleton,
                object : OrderHistoryResponseInterface {
                    override fun onResponse(orderHistoryElements: List<OrderHistoryElement>) {
                        viewModelScope.launch {
                            orderHistoryEvent.send(Event.LOADED)
                            orderHistory.postValue(orderHistoryElements)
                        }
                    }

                    override fun onError(message: String) {
                        viewModelScope.launch {
                            orderHistoryEvent.send(Event.LOADED)
                        }
                    }

                })
        } else {
            orderHistoryEvent.send(Event.OFFLINE)
        }

    }

    override fun getOrderHistory(): LiveData<List<OrderHistoryElement>> = orderHistory
    override fun getEvents(): LiveData<Event> = orderHistoryEvent.receiveAsFlow().asLiveData()


    enum class Event {
        LOADED, LOADING, OFFLINE
    }
}
