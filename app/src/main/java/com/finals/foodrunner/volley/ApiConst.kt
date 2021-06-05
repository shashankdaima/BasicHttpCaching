package com.finals.foodrunner.volley

sealed class ApiConst {
    companion object {
        const val TOKEN = "8f7008284e6201"

        const val ALL_RESTAURANTS_URL = "http://13.235.250.119/v2/restaurants/fetch_result/"
        const val LOGIN_URL = "http://13.235.250.119/v2/login/fetch_result/"
        const val REGISTRATION_URL = "http://13.235.250.119/v2/register/fetch_result"
        const val FORGOT_PASSWORD_URL = "http://13.235.250.119/v2/forgot_password/fetch_result"
        const val OTP_CONFIRM_URL = "http://13.235.250.119/v2/reset_password/fetch_result"
    }


}