package com.eltonls.temperodigital.activities.cart

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.eltonls.temperodigital.CustomBar
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.adapters.CartListAdapter
import com.eltonls.temperodigital.activities.cart.clickListeners.CartActivityClickListener
import com.eltonls.temperodigital.activities.menuList.MenuListActivity
import com.eltonls.temperodigital.databinding.ActivityCartBinding
import com.eltonls.temperodigital.models.Cart
import com.eltonls.temperodigital.models.MenuListItem

class CartActivity : AppCompatActivity(), CartActivityClickListener {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartItems: Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartItems = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_CART, Cart::class.java)!!
        } else {
            // Older version
            intent.getParcelableExtra<Cart>(MenuListActivity.INTENT_EXTRA_CART)!!
        }

        val cartListRecyclerView = binding.recyclerViewCart
        cartListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cartListRecyclerView.adapter = CartListAdapter(cartItems!!, this)

        // Set up the toolbar
        val customBar = CustomBar()
        customBar.showCustomToolbar(this, binding.toolbar)
    }

    override fun onDeleteItem(cartItem: MenuListItem) {
        cartItems.items.remove(cartItem)
    }
}