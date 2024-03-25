package com.eltonls.temperodigital.models

import android.os.Parcel
import android.os.Parcelable

class MenuListItem(
    val name: String,
    val price: Float,
    val imageUrl: String,
    val desc: String,
    val time: Int,
    var quantity: Int,
    var observation: String = " "
) : Parcelable {
    constructor(source: Parcel) : this (
        source.readString()!!,
        source.readFloat(),
        source.readString()!!,
        source.readString()!!,
        source.readInt(),
        source.readInt(),
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeFloat(price)
        dest.writeString(imageUrl)
        dest.writeString(desc)
        dest.writeInt(time)
        dest.writeInt(quantity)
        dest.writeString(observation)
    }

    companion object CREATOR : Parcelable.Creator<MenuListItem> {
        override fun createFromParcel(source: Parcel?): MenuListItem? = source?.let { MenuListItem(it) }
        override fun newArray(size: Int): Array<MenuListItem?> = arrayOfNulls(size)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + time
        result = 31 * result + quantity
        result = 31 * result + observation.hashCode()
        return result
    }
}