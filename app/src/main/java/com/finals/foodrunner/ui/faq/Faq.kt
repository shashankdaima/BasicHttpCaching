package com.finals.foodrunner.ui.faq

import androidx.fragment.app.Fragment
import com.finals.foodrunner.R
import android.os.Bundle
import android.view.Menu

class Faq:Fragment(R.layout.fragment_faq){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}