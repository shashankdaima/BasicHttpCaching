package com.finals.foodrunner.ui.confirmation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.ConfirmationFragmentBinding
import com.finals.foodrunner.ui.cart.CartViewModel
import com.finals.foodrunner.ui.cart.CartViewModelFactory
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.VolleySingleton

class ConfirmationFragment : Fragment(R.layout.confirmation_fragment) {
    private lateinit var binding: ConfirmationFragmentBinding
    private lateinit var viewModel: CartViewModel
    private val args: ConfirmationFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConfirmationFragmentBinding.bind(view)
        val volleySingleton = VolleySingleton(requireContext())
        val connectivityManager = ConnectivityManager(requireContext())
        val cartViewModelFactory =
            CartViewModelFactory(volleySingleton, connectivityManager, args.orderedFood)
        viewModel = ViewModelProvider(this, cartViewModelFactory).get(CartViewModel::class.java)

        

    }
}