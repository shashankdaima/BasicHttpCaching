package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.SORT_SCHEME

interface HomeViewModel {
    fun updateAllRestaurants(forceRefresh:Boolean=false);
    fun fetchFromDatabase()
    fun getSortOrderLiveData(): MutableLiveData<SORT_SCHEME>
    fun fetchAllRestaurants(): LiveData<List<Restaurant>>;
    fun sortOrderChanged(index:Int)

    fun restaurantFavChanged(restaurant:Restaurant)
}
interface MyProfileViewModel{

}
interface FavouriteRestaurant{

}
interface OrderHistory{

}
