package com.eltonls.temperodigital.activities.menuList.adapters

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.menuList.MenuListActivity
import com.eltonls.temperodigital.activities.menuList.viewHolders.MenuListItemViewHolder
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.databinding.CardMenuListItemHolderBinding
import com.eltonls.temperodigital.models.MenuListSection
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class MenuListSectionAdapter(
    private val menuList: MenuListSection,
    val clickListener: MenuListRecyclerViewClickListener
) :
    RecyclerView.Adapter<MenuListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListItemViewHolder {
        val binding = CardMenuListItemHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MenuListItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.items.size
    }

    override fun onBindViewHolder(holder: MenuListItemViewHolder, position: Int) {
        with(holder) {
            with(menuList.items[position]) {
                binding.root.setOnClickListener {
                    clickListener.onMenuItemClick(it, this)
                }
                binding.textMenuListItemName.text = this.name
                binding.textMenuListItemDesc.text = this.desc
                binding.textMenuListItemPrice.text =
                    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this.price)

                val displayMetrics = binding.root.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels

                Picasso.get()
                    .load(this.imageUrl)
                    .resize(screenWidth, 0)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.imageMenuListItem)
            }
        }
    }
}