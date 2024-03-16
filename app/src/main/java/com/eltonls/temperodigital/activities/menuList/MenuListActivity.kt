package com.eltonls.temperodigital.activities.menuList

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.CartActivity
import com.eltonls.temperodigital.activities.menuItemDetail.MenuItemDetail
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListAdapter
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListRecyclerViewClickListener
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.models.Cart
import com.eltonls.temperodigital.models.MenuList
import com.eltonls.temperodigital.models.MenuListItem
import com.google.gson.Gson

class MenuListActivity : AppCompatActivity(), MenuListRecyclerViewClickListener {
    private lateinit var menuList: MenuList
    private var cart = Cart()
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMenuListBinding

    companion object {
        const val INTENT_EXTRA_MENU_ITEM = "menuItem"
        const val INTENT_EXTRA_NEW_CART_ITEM = "newCartItem"
        const val INTENT_EXTRA_CART = "cart"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_TemperoDigital)
        super.onCreate(savedInstanceState)

        // Bind the activity layout
        binding = ActivityMenuListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                resultHandler(result)
            }
        menuList = loadMenuListData()

        // Create the menu List and adapter
        val menuListRecyclerView = binding.menuListRecyclerView
        menuListRecyclerView.adapter = MenuListAdapter(MenuList(menuList.sections), this)
        menuListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.buttonMenuCartOpen.setOnClickListener {
            this.openCart()
        }
    }

    override fun onMenuItemClick(view: View, payload: MenuListItem) {

        when(view.id) {
            R.id.card_menu_list_item -> {
                showMenuItemDetail(payload)
            }
            R.id.button_menu_cart_open -> {
                openCart()
            }
        }
    }

    private fun resultHandler(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val newMenuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(
                    INTENT_EXTRA_NEW_CART_ITEM,
                    MenuListItem::class.java
                )
            } else {
                // Deprecated in SDK 33
                result.data?.getParcelableExtra(INTENT_EXTRA_NEW_CART_ITEM)
            }

            cart.items.add(newMenuItem!!)
            cart.totalPrice = cart.totalPrice + (newMenuItem.price * newMenuItem.quantity)
        }
    }

    private fun loadMenuListData(): MenuList {
        val file = resources.assets.open("Mock.json")
        val json = file.bufferedReader().use { it.readText() }
        val gson = Gson()

        file.close()

        return gson.fromJson(json, MenuList::class.java)
    }

    private fun showMenuItemDetail(item: MenuListItem) {
        val menuItemIntent = Intent(this, MenuItemDetail::class.java)
        menuItemIntent.putExtra(INTENT_EXTRA_MENU_ITEM, item)
        launcher.launch(menuItemIntent)
    }

    private fun openCart() {
        val cartIntent = Intent(this, CartActivity::class.java)
        cartIntent.putExtra(INTENT_EXTRA_CART, cart)
        launcher.launch(cartIntent)
    }
}