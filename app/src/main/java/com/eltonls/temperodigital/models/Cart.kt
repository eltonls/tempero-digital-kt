package com.eltonls.temperodigital.models

import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import com.eltonls.temperodigital.activities.cart.clickListeners.CartActivityClickListener
import com.google.android.material.textfield.TextInputEditText

class Cart(var items : MutableList<MenuListItem> = mutableListOf()) : Parcelable, CartActivityClickListener {
    var totalPrice = 0.0f

    fun calculateTotalPrice() {
        totalPrice = 0.0f
        for (item in items) {
            totalPrice += item.price * item.quantity
        }
    }

    constructor(parcel: Parcel) : this() {
        // NEED TO TAKE A LOOK AT THIS AGAIN
        items =
            parcel.readArrayList(MenuListItem::class.java.classLoader) as MutableList<MenuListItem>
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

    override fun onDeleteItem(cartItem: MenuListItem) {
        items.remove(cartItem)
        calculateTotalPrice()
    }

    override fun onAddQuantity(cartItem: MenuListItem) {
        cartItem.quantity += 1
        calculateTotalPrice()
    }

    override fun onRemoveQuantity(cartItem: MenuListItem) {
        if (cartItem.quantity > 0) {
            cartItem.quantity -= 1
            calculateTotalPrice()
        } else {
            items.remove(cartItem)
        }
    }
}