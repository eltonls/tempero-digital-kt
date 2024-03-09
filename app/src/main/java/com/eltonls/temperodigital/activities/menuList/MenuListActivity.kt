package com.eltonls.temperodigital.activities.menuList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.menuList.adapters.MenuListAdapter
import com.eltonls.temperodigital.databinding.ActivityMenuListBinding
import com.eltonls.temperodigital.models.MenuList
import com.google.gson.Gson

class MenuListActivity : AppCompatActivity() {
    lateinit var menuList: MenuList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the activity layout
        val binding = ActivityMenuListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        menuList = loadMenuListData()

        // Create the menu List and adapter
        val menuListRecyclerView = binding.menuListRecyclerView
        menuListRecyclerView.adapter = MenuListAdapter(MenuList(menuList.sections))
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
}