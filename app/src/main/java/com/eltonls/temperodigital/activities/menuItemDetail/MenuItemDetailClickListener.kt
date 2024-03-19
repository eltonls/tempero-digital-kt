package com.eltonls.temperodigital.activities.menuItemDetail

import android.text.TextWatcher
import android.view.MenuItem
import com.eltonls.temperodigital.models.MenuListItem
import com.google.android.material.textfield.TextInputEditText

interface MenuItemDetailClickListener {
    fun onAddCounterMenuItemClick(item: TextInputEditText)
    fun onRemoveCounterMenuItemClick(item: TextInputEditText)
    fun onAddToCartMenuItemClick(menuItem: MenuListItem)
    fun onChangeQuantityMenuItem(item: TextInputEditText): TextWatcher
}