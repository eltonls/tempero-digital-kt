package com.eltonls.temperodigital.activities.cart.clickListeners

import android.text.TextWatcher
import com.eltonls.temperodigital.models.MenuListItem
import com.google.android.material.textfield.TextInputEditText

interface CartActivityClickListener {
    fun onDeleteItem(cartItem: MenuListItem)
    fun onAddQuantity(cartItem: MenuListItem)
    fun onRemoveQuantity(cartItem: MenuListItem)
}