package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import com.finals.foodrunner.objects.Restaurant

interface HomeViewModel {
    fun changeFavouriteStatus(restaurant: Restaurant)
    suspend fun fetchAllRestaurants()
    fun getRestaurants():LiveData<List<Restaurant>>

}
interface FavouriteViewModel{
    fun getFavRestaurants():LiveData<List<Restaurant>>
    fun unFavStatus(restaurant: Restaurant)

}