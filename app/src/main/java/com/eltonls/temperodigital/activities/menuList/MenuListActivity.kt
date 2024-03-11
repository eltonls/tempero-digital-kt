package com.eltonls.temperodigital.activities.menuList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.menuItemDetail.MenuItemDetail
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListAdapter
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListRecyclerViewClickListener
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.models.MenuList
import com.eltonls.temperodigital.models.MenuListItem
import com.google.gson.Gson

class MenuListActivity : AppCompatActivity(), MenuListRecyclerViewClickListener {
    lateinit var menuList: MenuList
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_TemperoDigital)
        super.onCreate(savedInstanceState)

        // Bind the activity layout
        val binding = ActivityMenuListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        menuList = loadMenuListData()

        // Create the menu List and adapter
        val menuListRecyclerView = binding.menuListRecyclerView
        menuListRecyclerView.adapter = MenuListAdapter(MenuList(menuList.sections), this)
        menuListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        startActivity(menuItemIntent)
    }

    companion object {
        const val INTENT_EXTRA_MENU_ITEM = "menuItem"
    }

    override fun onMenuItemClick(item: MenuListItem) {
       showMenuItemDetail(item)
    }
}