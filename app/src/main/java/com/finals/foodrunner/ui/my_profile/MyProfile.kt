package com.finals.foodrunner.ui.my_profile

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.finals.foodrunner.R

class MyProfile:Fragment(R.layout.fragment_my_profile){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}