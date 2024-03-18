package com.eltonls.temperodigital.activities.cart.clickListeners

import com.eltonls.temperodigital.models.MenuListItem

interface CartActivityClickListener {
    fun onDeleteItem(cartItem: MenuListItem)
}