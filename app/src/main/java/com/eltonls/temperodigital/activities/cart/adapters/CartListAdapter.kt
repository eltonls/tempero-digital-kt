package com.eltonls.temperodigital.activities.cart.adapters

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.viewHolders.CartItemViewHolder
import com.eltonls.temperodigital.databinding.CardCartItemViewHolderBinding
import com.eltonls.temperodigital.models.Cart
import com.squareup.picasso.Picasso
import java.util.Locale

class CartListAdapter(val cart: Cart) : RecyclerView.Adapter<CartItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding =
            CardCartItemViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CartItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cart.items.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        with(holder) {
            with(cart.items[position]) {
                val formatter = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault())
                val layoutManagerVertical = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

                binding.cardCartCounter.textinputCounter.text = Editable.Factory.getInstance().newEditable(this.quantity.toString())
                binding.textCartTitle.text = this.name
                binding.textCartObservation.text = this.desc // CHANGE THIS
                binding.textCartTotalPrice.text = formatter.format(this.price * this.quantity)

                val displayMetrics = binding.root.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels

                Picasso.get()
                    .load(this.imageUrl)
                    .resize(screenWidth, 0)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.imageCart)
            }
        }
    }
}