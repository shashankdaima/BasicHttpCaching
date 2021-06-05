package com.finals.foodrunner.ui.order_history

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.finals.foodrunner.R

class OrderHistory:Fragment(R.layout.fragment_order_history){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}