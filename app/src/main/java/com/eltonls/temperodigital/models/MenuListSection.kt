package com.eltonls.temperodigital.models

import java.util.UUID

class MenuListSection(val name: String, val items: MutableList<MenuListItem> = mutableListOf()) {
    val id = UUID.randomUUID().toString()
}