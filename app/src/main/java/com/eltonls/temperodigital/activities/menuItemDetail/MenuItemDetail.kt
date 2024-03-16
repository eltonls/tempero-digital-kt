package com.eltonls.temperodigital.activities.menuItemDetail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.menuList.MenuListActivity
import com.eltonls.temperodigital.databinding.ActivityMenuItemDetailBinding
import com.eltonls.temperodigital.models.MenuListItem
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class MenuItemDetail : AppCompatActivity(), MenuItemDetailClickListener {
    private var counter: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind the activity layout
        val binding = ActivityMenuItemDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val menuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra<MenuListItem>(MenuListActivity.INTENT_EXTRA_MENU_ITEM, MenuListItem::class.java)
        } else {
            // Deprecated in SDK 33
            intent.getParcelableExtra<MenuListItem>(MenuListActivity.INTENT_EXTRA_MENU_ITEM)
        }

        var counter = binding.textinputCounter

        counter.text = Editable.Factory.getInstance().newEditable("0")

        binding.buttonMenuItemDetailAdd.setOnClickListener {
            onAddCounterMenuItemClick(counter)
        }

        binding.buttonMenuItemDetailMinus.setOnClickListener {
            onRemoveCounterMenuItemClick(counter)
        }

        binding.buttonMenuItemDetailAddToCart.setOnClickListener {
            onAddToCartMenuItemClick(menuItem!!)
        }

        // Show menu item detail
        binding.textMenuItemDetailName.text = menuItem?.name
        binding.textMenuItemDetailDesc.text = menuItem?.desc
        binding.textMenuItemDetailPrice.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(menuItem?.price)

        val displayMetrics = binding.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        Picasso.get()
            .load(menuItem?.imageUrl)
            .resize(screenWidth, 0)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imageMenuItemDetail)
    }

    override fun onAddCounterMenuItemClick(item: TextInputEditText) {
        item.text = Editable.Factory.getInstance().newEditable((item.text.toString().toInt() + 1).toString())
        counter++
    }

    override fun onRemoveCounterMenuItemClick(item: TextInputEditText) {
        if (item.text.toString().toInt() > 0) {
            item.text = Editable.Factory.getInstance().newEditable((item.text.toString().toInt() - 1).toString())
            counter--
        }
    }

    override fun onAddToCartMenuItemClick(menuItem: MenuListItem) {
        val newCartItem = MenuListItem(menuItem.name, menuItem.price, menuItem.imageUrl, menuItem.desc, menuItem.time, counter)
        val resultIntent = Intent()
        resultIntent.putExtra(MenuListActivity.INTENT_EXTRA_NEW_CART_ITEM, newCartItem)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}