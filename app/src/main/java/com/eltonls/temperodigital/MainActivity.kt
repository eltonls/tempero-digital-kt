package com.eltonls.temperodigital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eltonls.temperodigital.models.Cart

class MainActivity : AppCompatActivity() {
    val cart = Cart()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}