 package com.finals.foodrunner.room

import androidx.room.*
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.util.exhaustive
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurant(list: List<Restaurant>)

    @Query("DELETE FROM RESTAURANTS ")
    fun deleteAllRestaurants()

    fun getAllRestaurants(searchQuery: String, sortBy: SORT_SCHEME): Flow<List<Restaurant>> {
        return when (sortBy) {
            SORT_SCHEME.SORT_BY_RATING -> getAllRestaurantSortByRating(searchQuery)
            SORT_SCHEME.SORT_BY_INC_COST-> getAllRestaurantSortByIncPrice(searchQuery)
            SORT_SCHEME.SORT_BY_DES_COST -> getAllRestaurantSortByDecPrice(searchQuery)
        }.exhaustive
    }


    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY rating DESC")
    fun getAllRestaurantSortByRating(searchQuery: String=""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one DESC")
    fun getAllRestaurantSortByDecPrice(searchQuery: String=""): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :searchQuery || '%' ORDER BY cost_for_one ASC")
    fun getAllRestaurantSortByIncPrice(searchQuery: String=""): Flow<List<Restaurant>>

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)

}
enum class SORT_SCHEME {
    SORT_BY_RATING, SORT_BY_INC_COST, SORT_BY_DES_COST
}
val SORT_MAP= mapOf<SORT_SCHEME,String>(
    SORT_SCHEME.SORT_BY_RATING to "Sort by Rating",
    SORT_SCHEME.SORT_BY_INC_COST to "Sort by Increasing Cost",
    SORT_SCHEME.SORT_BY_DES_COST to "Sort by Decreasing Cost"
).exhaustive
