package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.SORT_SCHEME
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface HomeViewModel {
    fun updateAllRestaurants(forceRefresh:Boolean=false);
    fun fetchFromDatabase(): Flow<List<Restaurant>>?
    fun getSortOrderLiveData(): MutableLiveData<SORT_SCHEME>
    fun  sortOrderChanged(index:Int)
    fun setQuery(query:String)
    fun getQuery():LiveData<String>
    fun reloadHome()

    fun getHomeEventChannel(): Channel<MainActivityViewModel.Events>

    fun restaurantFavChanged(restaurant:Restaurant)
}
interface MyProfileViewModel{

}
interface FavouriteRestaurant{

}
interface OrderHistory{

}
