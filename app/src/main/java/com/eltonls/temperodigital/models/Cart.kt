package com.eltonls.temperodigital.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.MenuItem
import com.eltonls.temperodigital.activities.menuList.MenuListActivity

class Cart() : Parcelable {
    var items: MutableList<MenuListItem> = mutableListOf()
    var totalPrice = 0.0f

    constructor(parcel: Parcel) : this() {
        // NEED TO TAKE A LOOK AT THIS AGAIN
        items = parcel.readArrayList(MenuListItem::class.java.classLoader) as MutableList<MenuListItem>
        totalPrice = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(items)
        parcel.writeFloat(totalPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}