package com.eltonls.temperodigital.activities.menuItemDetail

import android.text.TextWatcher
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.eltonls.temperodigital.models.MenuListItem
import com.google.android.material.textfield.TextInputEditText

interface MenuItemDetailClickListener {
    fun onAddCounterMenuItemClick(item: TextView)
    fun onRemoveCounterMenuItemClick(item: TextView)
    fun onAddToCartMenuItemClick(menuItem: MenuListItem)
    fun onChangeObservationText(item: AppCompatEditText): TextWatcher
}