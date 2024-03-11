package com.eltonls.temperodigital.activities.menuItemDetail

import android.view.MenuItem
import com.google.android.material.textfield.TextInputEditText

interface MenuItemDetailClickListener {
    fun onAddCounterMenuItemClick(item: TextInputEditText)
    fun onRemoveCounterMenuItemClick(item: TextInputEditText)
    fun onAddToCartMenuItemClick(menuItem: MenuItem)
}