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
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.eltonls.temperodigital.CustomBar
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.CartActivity
import com.eltonls.temperodigital.activities.menuItemDetail.MenuItemDetail
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListAdapter
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListRecyclerViewClickListener
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.fragments.CardCartFragment
import com.eltonls.temperodigital.models.Cart
import com.eltonls.temperodigital.models.MenuList
import com.eltonls.temperodigital.models.MenuListItem
import com.google.gson.Gson

class MenuListActivity : AppCompatActivity(), MenuListRecyclerViewClickListener {
    private lateinit var menuList: MenuList
    private var cart = Cart()
    private lateinit var menuItemDetailActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var cartActivityLauncher : ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMenuListBinding

    companion object {
        const val INTENT_EXTRA_MENU_ITEM = "menuItem"
        const val INTENT_EXTRA_NEW_CART_ITEM = "newCartItem"
        const val NEW_CART_ITEM_CODE = 1
        const val INTENT_EXTRA_CART = "cart"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_TemperoDigital)
        super.onCreate(savedInstanceState)

        // Bind the activity layout
        binding = ActivityMenuListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Activity Result Launchers
        cartActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> onCartResult(result)
        }

        menuItemDetailActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> onFullItemResult(result)
        }


        menuList = loadMenuListData()

        // Create the menu List and adapter
        val menuListRecyclerView = binding.menuListRecyclerView
        menuListRecyclerView.adapter = MenuListAdapter(MenuList(menuList.sections), this)
        menuListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        // Set up the toolbar
        val customBar = CustomBar()
        customBar.showCustomToolbar(this, binding.toolbar)
    }

    override fun onMenuItemClick(view: View, payload: MenuListItem) {

        when (view.id) {
            R.id.card_menu_list_item -> {
                showMenuItemDetail(payload)
            }
        }
    }

    override fun onResume() {
        rebuildFragment()
        super.onResume()
    }

    fun onCartResult(result: ActivityResult) {
        if(result.resultCode == RESULT_OK) {
           val newCart = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
               result.data?.getParcelableExtra(INTENT_EXTRA_CART, Cart::class.java)
           } else {
                result.data?.getParcelableExtra(INTENT_EXTRA_CART)
           }

            if(newCart != null) {
                cart.items.clear()
                cart.items.addAll(newCart.items)
                cart.calculateTotalPrice()
            }
        }
    }

    fun onFullItemResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val newMenuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(
                    INTENT_EXTRA_NEW_CART_ITEM, MenuListItem::class.java
                )
            } else {
                // Deprecated in SDK 33
                result.data?.getParcelableExtra(INTENT_EXTRA_NEW_CART_ITEM)
            }

            if(cart.items.contains(newMenuItem))
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
        menuItemDetailActivityLauncher.launch(menuItemIntent)
    }

    fun launchCart() {
        val cartIntent = Intent(this, CartActivity::class.java)
        cartIntent.putExtra(INTENT_EXTRA_CART, cart)
        cartActivityLauncher.launch(cartIntent)
    }

    private fun showCartCard() {
        if (cart.items.size > 0) {
            var totalPriceBundle = bundleOf("totalPrice" to cart.totalPrice)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CardCartFragment>(R.id.fragment_card_cart_container, args = totalPriceBundle)
            }
        }
    }

    private fun rebuildFragment() {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_card_cart_container)

        existingFragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        showCartCard()
    }
}