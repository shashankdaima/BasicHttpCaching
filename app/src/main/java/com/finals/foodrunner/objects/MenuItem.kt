package com.finals.foodrunner.objects

import android.os.Parcel
import android.os.Parcelable

data class MenuItem(
    val sno: Int,
    val name: String?,
    val price: Int,
    val restaurantId: Int,
    var isOrder:Boolean=false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sno)
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeInt(restaurantId)
        parcel.writeByte(if (isOrder) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuItem> {
        override fun createFromParcel(parcel: Parcel): MenuItem {
            return MenuItem(parcel)
        }

        override fun newArray(size: Int): Array<MenuItem?> {
            return arrayOfNulls(size)
        }
    }
}
