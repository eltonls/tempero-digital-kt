package com.eltonls.temperodigital.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eltonls.temperodigital.R
import com.eltonls.temperodigital.activities.menuList.MenuListActivity
import com.eltonls.temperodigital.databinding.FragmentCardCartBinding
import java.util.Locale

class CardCartFragment : Fragment() {
    lateinit private var binding: FragmentCardCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCardCartBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MenuListActivity
        val formatter = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault())
        // Inflate the layout for this fragment
        val textTotalPrice = binding.textCartCardTotal
        val totalPrice = arguments?.getFloat("totalPrice")

        binding.buttonOpenCart.setOnClickListener() {
            activity.openCart()
        }

        if(totalPrice != null) {
            textTotalPrice.text = formatter.format(totalPrice)
        }

        return binding.root
    }
}