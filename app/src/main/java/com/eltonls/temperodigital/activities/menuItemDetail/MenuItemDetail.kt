package com.eltonls.temperodigital.activities.menuItemDetail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
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
    private lateinit var binding: ActivityMenuItemDetailBinding
    private lateinit var counterView: TextInputEditText
    private lateinit var menuItem: MenuListItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind the activity layout
        binding = ActivityMenuItemDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        menuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra<MenuListItem>(
                MenuListActivity.INTENT_EXTRA_MENU_ITEM,
                MenuListItem::class.java
            )!!
        } else {
            // Deprecated in SDK 33
            intent.getParcelableExtra<MenuListItem>(MenuListActivity.INTENT_EXTRA_MENU_ITEM)!!
        }

        counterView = binding.cardCounter.textinputCounter

        counterView.text = Editable.Factory.getInstance().newEditable("0")

        // Show menu item detail
        binding.textMenuItemDetailName.text = menuItem?.name
        binding.textMenuItemDetailDesc.text = menuItem?.desc
        binding.textMenuItemDetailPrice.text =
            NumberFormat.getCurrencyInstance(Locale.getDefault()).format(menuItem?.price)

        val displayMetrics = binding.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        Picasso.get().load(menuItem?.imageUrl).resize(screenWidth, 0)
            .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageMenuItemDetail)

        eventListenersSet()
    }

    private fun eventListenersSet() {
        binding.cardCounter.textinputCounter.addTextChangedListener(onChangeQuantityMenuItem(counterView))

        binding.cardCounter.buttonMenuItemDetailAdd.setOnClickListener {
            onAddCounterMenuItemClick(counterView)
        }

        binding.cardCounter.buttonMenuItemDetailMinus.setOnClickListener {
            onRemoveCounterMenuItemClick(counterView)
        }

        binding.buttonMenuItemDetailAddToCart.setOnClickListener {
            onAddToCartMenuItemClick(menuItem!!)
        }

    }

    override fun onChangeQuantityMenuItem(item: TextInputEditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    counter = 0
                    return
                }
                counter = s.toString().toInt()
            }
        }
    }

    override fun onAddCounterMenuItemClick(item: TextInputEditText) {
        item.text = Editable.Factory.getInstance()
            .newEditable((item.text.toString().toInt() + 1).toString())
        counter++
    }

    override fun onRemoveCounterMenuItemClick(item: TextInputEditText) {
        if (item.text.toString().toInt() > 0) {
            item.text = Editable.Factory.getInstance()
                .newEditable((item.text.toString().toInt() - 1).toString())
            counter--
        }
    }

    override fun onAddToCartMenuItemClick(menuItem: MenuListItem) {
        if (counter > 0) {
            val newCartItem = MenuListItem(
                menuItem.name,
                menuItem.price,
                menuItem.imageUrl,
                menuItem.desc,
                menuItem.time,
                counterView.text.toString().toInt()
            )
            val resultIntent = Intent()
            resultIntent.putExtra(MenuListActivity.INTENT_EXTRA_NEW_CART_ITEM, newCartItem)
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Adicione ao menos um item", Toast.LENGTH_SHORT).show()
        }
    }
}