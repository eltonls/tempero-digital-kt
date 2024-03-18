package com.eltonls.temperodigital

import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.eltonls.temperodigital.databinding.ToolbarMainBinding

class CustomBar {
    fun showCustomToolbar(activity: AppCompatActivity, toolbar: androidx.appcompat.widget.Toolbar) {
        val toolbarBinding = ToolbarMainBinding.inflate(activity.layoutInflater)

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
    }
}