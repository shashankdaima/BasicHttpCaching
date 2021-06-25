package com.finals.foodrunner.volley

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.objects.User
import org.json.JSONObject


fun getAllRestaurants(
    volleySingleton: VolleySingleton,
    restaurantResponseInterface: RestaurantResponseInterface,

    ) {
    val jsonObjectRequest = object : JsonObjectRequest(
        Method.GET,
        ALL_RESTAURANTS_URL,
        null,
        Response.Listener {
            try {
                val obj = it.getJSONObject("data");
                val list = ArrayList<Restaurant>();
                if (obj.getBoolean("success")) {
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val restaurantJsonData = data.getJSONObject(i)
                        val restaurantObject = Restaurant(
                            name = restaurantJsonData.getString("name"),
                            id = restaurantJsonData.getInt("id"),
                            rating = restaurantJsonData.getString("rating"),
                            cost_for_one = restaurantJsonData.getInt("cost_for_one"),
                            image_url = restaurantJsonData.getString("image_url")
                        )
                        list.add(restaurantObject)
                    }

                    restaurantResponseInterface.onResponse(list)
                } else {

                    restaurantResponseInterface.onError(Throwable("Unknown Error"), list)
                }


            } catch (e: Exception) {

                restaurantResponseInterface.onError(e)
            }
        },
        Response.ErrorListener {

            restaurantResponseInterface.onError(it)
        }) {
        override fun getHeaders(): MutableMap<String, String> {
            return HEADER
        }
    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}

fun getMenu(
    volleySingleton: VolleySingleton,
    restaurant: Restaurant,
    menuResponseInterface: MenuResponseInterface
) {
    val jsonObjectRequest = object : JsonObjectRequest(
        Method.GET,
        RESTAURANT_MENU + restaurant.id.toString(),
        null,
        {
            try {
                val obj = it.getJSONObject("data");
                if (obj.getBoolean("success")) {
                    val list = mutableListOf<MenuItem>()
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val foodJsonData = data.getJSONObject(i)
                        val foodItem = MenuItem(
                            name = foodJsonData.getString("name"),
                            sno = foodJsonData.getInt("id"),
                            price = foodJsonData.getInt("cost_for_one"),
                            restaurantId = foodJsonData.getInt("restaurant_id"),
                        )
                        list.add(foodItem)
                    }
                    menuResponseInterface.onResponse(list)

                } else {
                    menuResponseInterface.onError("Unknown Error Happened")
                }


            } catch (e: Exception) {
                menuResponseInterface.onError(e.localizedMessage!!)

            }

        }, {
            menuResponseInterface.onError(it.localizedMessage!!)

        }) {
        override fun getHeaders(): MutableMap<String, String> {
            return HEADER
        }


    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}

fun login(
    mobileNumber: String,
    password: String,
    volleySingleton: VolleySingleton,
    onLoginResponseInterface: LoginResponseInterface
) {
    val credential = JSONObject(
        mapOf(
            "mobile_number" to mobileNumber,
            "password" to password
        )
    )
    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, LOGIN_URL, credential, {
        val data = it.getJSONObject("data")
        val success = data.getBoolean("success")
        if (success) {
            val response = data.getJSONObject("data")
            val user = User(
                user_id = response.getLong("user_id"),
                name = response.getString("name"),
                email = response.getString("email"),
                address = response.getString("address"),
                mobile_number = response.getLong("mobile_number")
            )
            onLoginResponseInterface.onResponse(user)

        } else {
            onLoginResponseInterface.onError("Something went Wrong")
        }
    }, {
        onLoginResponseInterface.onError(it.localizedMessage)
    }) {
        override fun getHeaders() = HEADER

    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}



