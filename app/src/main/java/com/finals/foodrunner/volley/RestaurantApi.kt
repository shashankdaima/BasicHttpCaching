package com.finals.foodrunner.volley

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.finals.foodrunner.objects.*
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

                    restaurantResponseInterface.onError(Throwable("Unknown Error:API error"), list)
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
                    menuResponseInterface.onError(obj.getString("errorMessage"))
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
    onUserAuthResponseInterface: UserAuthResponseInterface
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
            onUserAuthResponseInterface.onResponse(user)

        } else {
            onUserAuthResponseInterface.onError(data.getString("errorMessage"))
        }
    }, {
        onUserAuthResponseInterface.onError(it.localizedMessage)
    }) {
        override fun getHeaders() = HEADER

    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}

fun signUp(
    name: String,
    mobileNumber: String,
    password: String,
    address: String,
    email: String,
    volleySingleton: VolleySingleton,
    onUserAuthResponseInterface: UserAuthResponseInterface
) {
    val credential = JSONObject(
        mapOf(
            "name" to name,
            "mobile_number" to mobileNumber,
            "password" to password,
            "address" to address,
            "email" to email
        )
    )
    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, REGISTRATION_URL, credential, {
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
            onUserAuthResponseInterface.onResponse(user)

        } else {
            onUserAuthResponseInterface.onError(data.getString("errorMessage"))
        }
    }, {
        onUserAuthResponseInterface.onError(it.localizedMessage)
    }) {
        override fun getHeaders() = HEADER

    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}

fun order(
    userId: Long,
    restaurantId: Int,
    totalCost: Int,
    foods: Array<Int?>,
    volleySingleton: VolleySingleton,
    orderResponseInterface: OrderResponseInterface
) {
    val foodArray = arrayListOf<Map<String, String>>()
    for (i in foods) {
        if (i != null) {
            val ifood = mapOf(
                "food_item_id" to i.toString()
            )
            foodArray.add(ifood)
        }
    }
    val params = JSONObject(
        mapOf(
            "user_id" to userId.toString(),
            "restaurant_id" to restaurantId.toString(),
            "total_cost" to totalCost.toString(),
            "food" to foodArray
        )
    )
    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, PLACE_ORDER_URL, params, {
        try {
            val data = it.getJSONObject("data")
            val success = data.getBoolean("success")
            if (success) {
                orderResponseInterface.onResponse()
            } else {
                orderResponseInterface.onError(data.getString("errorMessage"))
            }
        } catch (e: Exception) {
            orderResponseInterface.onError(e.localizedMessage)
        }
    }, {
        orderResponseInterface.onError(it.localizedMessage)
    }) {
        override fun getHeaders() = HEADER
    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)


}

fun getOrderHistory(
    userId: String,
    volleySingleton: VolleySingleton,
    orderHistoryResponseInterface: OrderHistoryResponseInterface

) {
    val jsonObjectRequest = object : JsonObjectRequest(
        Method.GET,
        ORDER_HISTORY_URL + userId,
        null,
        {
            try {
                val data = it.getJSONObject("data");
                if (data.getBoolean("success")) {
                    val dataArray = data.getJSONArray("data");
                    val result = mutableListOf<OrderHistoryElement>()
                    for (i in 0 until dataArray.length()) {
                        val iJsonObject = dataArray.getJSONObject(i)
                        val iJsonObjectFoodItems = iJsonObject.getJSONArray("food_items")
                        val foodItemArray = mutableListOf<FoodItem>()
                        for (j in 0 until iJsonObjectFoodItems.length()) {
                            val iFoodItem = iJsonObjectFoodItems.getJSONObject(j)
                            val foodItem = FoodItem(
                                name = iFoodItem.getString("name"),
                                food_item_id = iFoodItem.getString("food_item_id"),
                                cost = iFoodItem.getString("cost"),

                                )
                            foodItemArray.add(foodItem)
                        }
                        val orderHistoryElement = OrderHistoryElement(
                            order_id = iJsonObject.getString("order_id"),
                            restaurant_name = iJsonObject.getString("restaurant_name"),
                            total_cost = iJsonObject.getString("total_cost"),
                            order_placed_at = iJsonObject.getString("order_placed_at"),
                            food_items = foodItemArray
                        )
                        result.add(orderHistoryElement)

                    }
                    orderHistoryResponseInterface.onResponse(result)

                } else {
                    orderHistoryResponseInterface.onError(data.getString("errorMessage"))
                }


            } catch (e: Exception) {
                orderHistoryResponseInterface.onError(e.localizedMessage!!)

            }

        }, {
            orderHistoryResponseInterface.onError(it.localizedMessage!!)

        }) {
        override fun getHeaders() = HEADER


    }
    volleySingleton.addToRequestQueue(jsonObjectRequest)

}

fun submitCreditentials(
    jsonParams: JSONObject,
    volleySingleton: VolleySingleton,
    forgotPasswordResponseInterface: ForgotPasswordResponseInterface

) {
    val jsonObjectRequest =
        object : JsonObjectRequest(Method.POST, FORGOT_PASSWORD_URL, jsonParams, {

            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    val firstTry = data.getBoolean("first_try")
                    forgotPasswordResponseInterface.onResponse(firstTry)
                } else {
                    forgotPasswordResponseInterface.onError(data.getString("errorMessage"))

                }
            } catch (e: Exception) {
                forgotPasswordResponseInterface.onError(e.localizedMessage)

            }
        }, {
            forgotPasswordResponseInterface.onError(it.localizedMessage)

        }) {
            override fun getHeaders() = HEADER
        }
    volleySingleton.addToRequestQueue(jsonObjectRequest)
}

fun otpVerifyAndSubmitPassword(
    volleySingleton: VolleySingleton,
    jsonParams: JSONObject,
    otpVerificationAndPasswordSubmission: OtpVerificationAndPasswordSubmission
) {
    val jsonObjectRequest =
        object : JsonObjectRequest(Method.POST, RESET_PASSWORD_URL, jsonParams, {

            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    otpVerificationAndPasswordSubmission.onResponse(data.getString("successMessage"))
                } else {
                    otpVerificationAndPasswordSubmission.onError(data.getString("errorMessage"))

                }
            } catch (e: Exception) {
                otpVerificationAndPasswordSubmission.onError(e.localizedMessage)

            }
        }, {
            otpVerificationAndPasswordSubmission.onError(it.localizedMessage)

        }) {
            override fun getHeaders() = HEADER
        }
    volleySingleton.addToRequestQueue(jsonObjectRequest)


}