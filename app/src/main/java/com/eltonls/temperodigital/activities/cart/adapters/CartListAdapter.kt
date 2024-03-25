package com.eltonls.temperodigital.activities.cart.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.clickListeners.CartActivityClickListener
import com.eltonls.temperodigital.activities.cart.viewHolders.CartItemViewHolder
import com.eltonls.temperodigital.databinding.CardCartItemViewHolderBinding
import com.eltonls.temperodigital.models.Cart
import com.eltonls.temperodigital.models.MenuListItem
import com.squareup.picasso.Picasso
import java.util.Locale

class CartListAdapter(val cart: Cart, private val clickListener: CartActivityClickListener, private val updateTotalPrice: () -> Unit) :
    ListAdapter<MenuListItem, CartItemViewHolder>(CartDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CardCartItemViewHolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return CartItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cart.items.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        with(holder) {
            with(cart.items[position]) {
                val layoutManagerVertical =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

                holder.binding.buttonCartDelete.setOnClickListener {
                    clickListener.onDeleteItem(this)
                    notifyItemRemoved(position)
                    updateTotalPrice()
                }
                holder.bind(this, clickListener, position, updateQuantityLambda)
            }
        }
    }

    val updateQuantityLambda: (item: MenuListItem, quantity: Int, position: Int) -> Unit =
        { item, quantity, position ->
            item.quantity = quantity
            notifyItemChanged(position)
            updateTotalPrice()
        }

    internal class CartDiffCallback : DiffUtil.ItemCallback<MenuListItem>() {
        override fun areItemsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean {
            // Quantity is the only thing that changes in that phase
            return oldItem.quantity == newItem.quantity
        }
    }
}
