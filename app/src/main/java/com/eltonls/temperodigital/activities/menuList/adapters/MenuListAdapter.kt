package com.eltonls.temperodigital.activities.menuList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.activities.menuList.viewHolders.MenuListViewHolder
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.databinding.LayoutMenuListSectionBinding
import com.eltonls.temperodigital.models.MenuList

class MenuListAdapter(
    private val menu: MenuList,
    val clickListener: MenuListRecyclerViewClickListener
) : RecyclerView.Adapter<MenuListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding =
            LayoutMenuListSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MenuListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menu.sections.size
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        with(holder) {
            with(menu.sections[position]) {
                val layoutManagerHorizontal =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

                binding.textMenuSectionTitle.text = this.name
                binding.recyclerMenuListSection.adapter = MenuListSectionAdapter(this, clickListener)
                binding.recyclerMenuListSection.layoutManager = layoutManagerHorizontal
            }
        }
    }
}