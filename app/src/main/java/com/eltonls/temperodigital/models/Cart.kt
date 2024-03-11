package com.eltonls.temperodigital.models

import android.view.MenuItem

class Cart {
    var items: MutableList<MenuItem> = mutableListOf()
    var totalPrice = 0.0f
}