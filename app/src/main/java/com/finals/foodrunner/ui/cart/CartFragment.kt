package com.finals.foodrunner.ui.cart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.CartAdapter
import com.finals.foodrunner.databinding.CartFragmentBinding

class CartFragment : Fragment(R.layout.cart_fragment) {
    private lateinit var binding: CartFragmentBinding
    private val args: CartFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CartFragmentBinding.bind(view)
        val adapter = CartAdapter();


        adapter.submitList(args.orderedFood.toMutableList())
        binding.cartList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.restaurantName.text="Buy from ${args.restuarant.name}"
        var costSum=0;
        for(i in args.orderedFood){
            costSum+=i.price
        }
        binding.placeOrderButton.apply{
            text="Place Order(₹$costSum/-)"
            setOnClickListener {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToConfirmationFragment2(args.orderedFood))
            }
        }




    }
}