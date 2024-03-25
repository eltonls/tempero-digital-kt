package com.eltonls.temperodigital.activities.cart.viewHolders

import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.cart.clickListeners.CartActivityClickListener
import com.eltonls.temperodigital.databinding.CardCartItemViewHolderBinding
import com.eltonls.temperodigital.models.MenuListItem
import com.squareup.picasso.Picasso
import java.util.Locale

class CartItemViewHolder(val binding: CardCartItemViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: MenuListItem,
        clickListener: CartActivityClickListener,
        position: Int,
        onQuantityUpdate: (item: MenuListItem, quantity: Int, position: Int) -> Unit
    ) {
        bindTextInputCounter(item.quantity)
        bindTextCartTitle(item.name)
        bindTextCartObservation(item.observation)
        bindTextTotalPrice(item.price, item.quantity)
        bindTextEditQuantity(item.quantity)
        bindImageCart(item.imageUrl)

        bindAddButton(item, clickListener, position, onQuantityUpdate)
        bindMinusButton(item, clickListener, position, onQuantityUpdate)
    }

    private fun bindTextInputCounter(itemCounter: Int) {
        binding.cardCartCounter.textCounter.text = itemCounter.toString()
    }

    private fun bindTextEditQuantity(itemCounter: Int) {
        binding.cardCartCounter.textCounter.text = itemCounter.toString()
    }

    private fun bindTextCartTitle(itemTitle: String) {
        binding.textCartTitle.text = itemTitle
    }

    private fun bindTextCartObservation(itemDesc: String) {
        binding.textCartObservation.text = itemDesc
    }

    private fun bindTextTotalPrice(itemPrice: Float, itemQuantity: Int) {
        val formatter = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault())

        binding.textCartTotalPrice.text = formatter.format(itemPrice * itemQuantity)
    }

    private fun bindAddButton(
        item: MenuListItem,
        clickListener: CartActivityClickListener,
        position: Int,
        onQuantityUpdate: (item: MenuListItem, quantity: Int, position: Int) -> Unit
    ) {
        binding.cardCartCounter.buttonMenuItemDetailAdd.setOnClickListener {
            clickListener.onAddQuantity(item)
            onQuantityUpdate(item, item.quantity, position)
        }
    }

    private fun bindMinusButton(
        item: MenuListItem,
        clickListener: CartActivityClickListener,
        position: Int,
        onQuantityUpdate: (item: MenuListItem, quantity: Int, position: Int) -> Unit
    ) {
        binding.cardCartCounter.buttonMenuItemDetailMinus.setOnClickListener {
            clickListener.onRemoveQuantity(item)
            onQuantityUpdate(item, item.quantity, position)
        }
    }

    private fun bindImageCart(imageUrl: String) {
        val displayMetrics = binding.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        Picasso.get().load(imageUrl).resize(screenWidth, 0)
            .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageCart)
    }
}