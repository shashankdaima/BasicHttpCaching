package com.finals.foodrunner.volley

import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.objects.User

interface RestaurantResponseInterface {
    fun onError(throwable: Throwable, list: List<Restaurant>? = null)
    fun onResponse(list: List<Restaurant>)
}

interface MenuResponseInterface {
    fun onError(message: String)
    fun onResponse(list: List<MenuItem>)
}
interface LoginResponseInterface{
    fun onError(message: String)
    fun onResponse(user: User)
}
