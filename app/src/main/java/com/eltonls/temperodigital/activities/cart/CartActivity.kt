package com.eltonls.temperodigital.activities.cart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eltonls.temperodigital.CustomBar
import com.eltonls.temperodigital.activities.cart.adapters.CartListAdapter
import com.eltonls.temperodigital.activities.menuList.MenuListActivity
import com.eltonls.temperodigital.databinding.ActivityCartBinding
import com.eltonls.temperodigital.models.Cart
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartItems: Cart
    private lateinit var counterView: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartItems = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_CART, Cart::class.java)!!
        } else {
            // Older version
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_CART)!!
        }

        val cartListRecyclerView = binding.recyclerViewCart
        cartListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cartListRecyclerView.adapter = CartListAdapter(cartItems, cartItems, updateTotalPrice)

        binding.textTotalPrice.text = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault()).format(cartItems.totalPrice)

        // Set up the toolbar
        val customBar = CustomBar()
        customBar.showCustomToolbar(this, binding.toolbar)

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendCartData()
            }
        }

        onBackPressedDispatcher.addCallback(this, callBack)

        // Set up Finish Order Button
        binding.buttonCartFinish.setOnClickListener {
            cartItems.items.clear()
            Toast.makeText(this, "Seu pedido foi enviado à nossa cozinha!", Toast.LENGTH_LONG).show()
            sendCartData()
        }
    }

    private fun sendCartData() {
        val resultIntent = Intent()
        resultIntent.putExtra(MenuListActivity.INTENT_EXTRA_CART, cartItems)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    val updateTotalPrice: () -> Unit ={
        binding.textTotalPrice.text = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault()).format(cartItems.totalPrice)
    }
}