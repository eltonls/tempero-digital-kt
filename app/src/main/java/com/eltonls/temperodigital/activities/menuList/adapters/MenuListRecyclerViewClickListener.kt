package com.eltonls.temperodigital.activities.menuList.adapters

import android.view.View
import com.eltonls.temperodigital.models.MenuListItem

interface MenuListRecyclerViewClickListener {
    fun onMenuItemClick(view: View, payload: MenuListItem)
}