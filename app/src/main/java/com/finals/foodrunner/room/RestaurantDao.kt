package com.finals.foodrunner.room

import androidx.room.*
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.util.SORT_SCHEME
import com.finals.foodrunner.util.exhaustive
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(list: List<Restaurant>)

    @Query("DELETE FROM RESTAURANTS WHERE isFavourite!=1")
    suspend fun deleteUnFavouriteRestaurants()

    @Query("DELETE FROM RESTAURANTS")
    suspend fun deleteAllRestaurants();

    fun getAllRestaurants(searchQuery: String, sortBy: SORT_SCHEME): Flow<List<Restaurant>> {
        return when (sortBy) {
            SORT_SCHEME.SORT_BY_RATING -> getAllRestaurantSortByRating(searchQuery)
            SORT_SCHEME.SORT_BY_INC_COST -> getAllRestaurantSortByIncPrice(searchQuery)
            SORT_SCHEME.SORT_BY_DES_COST -> getAllRestaurantSortByDecPrice(searchQuery)
        }.exhaustive
    }
    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY rating DESC")
    fun getAllRestaurantSortByRating(searchQuery: String = ""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one DESC")
    fun getAllRestaurantSortByDecPrice(searchQuery: String = ""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one ASC")
    fun getAllRestaurantSortByIncPrice(searchQuery: String = ""): Flow<List<Restaurant>>
//    _______________________________________________________________________________________________________________


    fun getFavRestaurants(searchQuery: String, sortBy: SORT_SCHEME): Flow<List<Restaurant>> {
        return when (sortBy) {
            SORT_SCHEME.SORT_BY_RATING -> getFavRestaurantSortByRating(searchQuery)
            SORT_SCHEME.SORT_BY_INC_COST -> getFavRestaurantSortByIncPrice(searchQuery)
            SORT_SCHEME.SORT_BY_DES_COST -> getFavRestaurantSortByDecPrice(searchQuery)
        }.exhaustive
    }
    @Query("SELECT * FROM restaurants WHERE isFavourite==1 and  name LIKE '%' || :searchQuery || '%' ORDER BY rating DESC")
    fun getFavRestaurantSortByRating(searchQuery: String = ""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE isFavourite==1 and  name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one DESC")
    fun getFavRestaurantSortByDecPrice(searchQuery: String = ""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE isFavourite==1 and name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one ASC")
    fun getFavRestaurantSortByIncPrice(searchQuery: String = ""): Flow<List<Restaurant>>

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)

}
