package com.finals.foodrunner.volley

import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.objects.OrderHistoryElement
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
interface UserAuthResponseInterface{
    fun onError(message: String)
    fun onResponse(user: User)
}
interface OrderResponseInterface{
    fun onResponse()
    fun onError(message: String)
}

interface OrderHistoryResponseInterface{
    fun onResponse(orderHistoryElements: List<OrderHistoryElement>)
    fun onError(message: String)
}

interface ForgotPasswordResponseInterface{
    fun onResponse(isFirstTime:Boolean)
    fun onError(message: String)
}
interface OtpVerificationAndPasswordSubmission{
    fun onResponse(message:String)
    fun onError(message: String)
}