package com.eltonls.temperodigital.activities.menuList.adapters

import com.eltonls.temperodigital.models.MenuListItem

interface MenuListRecyclerViewClickListener {
    fun onMenuItemClick(item: MenuListItem)
}