package com.eltonls.temperodigital.models

import android.os.Parcel
import android.os.Parcelable

class MenuListItem(
    val name: String,
    val price: Float,
    val imageUrl: String,
    val desc: String,
    val time: Int
) : Parcelable {
    constructor(source: Parcel) : this (
        source.readString()!!,
        source.readFloat(),
        source.readString()!!,
        source.readString()!!,
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeFloat(price)
        dest.writeString(imageUrl)
        dest.writeString(desc)
        dest.writeInt(time)
    }

    companion object CREATOR : Parcelable.Creator<MenuListItem> {
        override fun createFromParcel(source: Parcel?): MenuListItem? = source?.let { MenuListItem(it) }
        override fun newArray(size: Int): Array<MenuListItem?> = arrayOfNulls(size)
    }
}