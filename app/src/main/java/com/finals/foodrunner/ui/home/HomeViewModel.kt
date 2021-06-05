package com.finals.foodrunner.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.finals.foodrunner.objects.RestaurantObject
import com.finals.foodrunner.volley.ApiConst
import org.json.JSONException


class HomeViewModel : ViewModel() {
    private val _restaurantList = MutableLiveData<ArrayList<RestaurantObject>>()
    val list: LiveData<ArrayList<RestaurantObject>> = _restaurantList
    val accessTokenRequest: JsonObjectRequest = object : JsonObjectRequest(
        Method.GET, ApiConst.ALL_RESTAURANTS_URL, null,
        Response.Listener {
            try {
                val obj = it.getJSONObject("data")
                val success = obj.getBoolean("success")
                if (success) {
                    val data = obj.getJSONArray("data")
                    val tempList = ArrayList<RestaurantObject>();
                    for (i in 0 until data.length()) {
                        val restaurantJsonObject = data.getJSONObject(i)
                        val restaurantObject = RestaurantObject(

                            id = restaurantJsonObject.getInt("id"),
                            name = restaurantJsonObject.getString("name"),
                            rating = restaurantJsonObject.getDouble("rating"),
                            cost = restaurantJsonObject.getInt("cost_for_one"),
                            isFavourite = false,
                            photo = restaurantJsonObject.getString("image_url")

                        )
                        tempList.add(restaurantObject)


                    }
                    this._restaurantList.value?.addAll(tempList)
                } else {
                    TODO("Make a toast Out of here")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        }, Response.ErrorListener {
            Log.e("Home Fragment", it.message.toString())
        }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["token"] = ApiConst.TOKEN
            params["Content-type"] = "application/json"
            return params
        }
    }



}