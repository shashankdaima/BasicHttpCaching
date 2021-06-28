package com.finals.foodrunner.ui.activity

import androidx.lifecycle.LiveData
import com.finals.foodrunner.objects.OrderHistoryElement
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.objects.User
import com.finals.foodrunner.util.SORT_SCHEME
import kotlinx.coroutines.flow.Flow

interface HomeViewModel {
    fun changeFavouriteStatus(restaurant: Restaurant)
    suspend fun fetchAllRestaurants()
    suspend fun setCurrentUser(user: User)
    suspend fun currentUser():LiveData<User>
    fun getRestaurants():LiveData<List<Restaurant>>
    fun getHomeEvents(): Flow<ActivityViewModel.Event>
    fun setHomeSortOrder(sortScheme: SORT_SCHEME)
    fun getHomeSortOrder():SORT_SCHEME
    fun setHomeSearchQuery(query:String)



}
interface ProfileViewModel{
    suspend fun currentUser():LiveData<User>

}

interface FavouriteViewModel{
    fun getFavRestaurants():LiveData<List<Restaurant>>
    fun unFavStatus(restaurant: Restaurant)
    suspend fun currentUser():LiveData<User>

    fun setFavSortOrder(sortScheme: SORT_SCHEME)
    fun getFavSortOrder():SORT_SCHEME
    fun setFavSearchQuery(query:String)


}
interface OrderHistoryViewModel{
    suspend fun fetchOrderHistory()
    fun getOrderHistory():LiveData<List<OrderHistoryElement>>
    fun getEvents():LiveData<ActivityViewModel.Event>
    suspend fun currentUser():LiveData<User>

}