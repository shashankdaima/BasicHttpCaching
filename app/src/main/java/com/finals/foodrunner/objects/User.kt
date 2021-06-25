package com.finals.foodrunner.objects

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class User(
    val user_id:Long,
    val name: String?,
    val email: String?,
    val mobile_number:Long,
    val address: String?
    )