package com.finals.foodrunner.ui.menu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finals.foodrunner.R

class MenuFragment : Fragment(R.layout.restaurant_menu_layout) {


    private lateinit var viewModel: MenuViewModel



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
    }

}