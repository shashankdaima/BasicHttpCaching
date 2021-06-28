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
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(user_id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeLong(mobile_number)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}