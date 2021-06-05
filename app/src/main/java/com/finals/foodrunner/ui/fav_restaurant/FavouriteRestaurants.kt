package com.finals.foodrunner.ui.fav_restaurant

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.objects.RestaurantObject
import com.finals.foodrunner.databinding.FragmentFavRestaurantBinding

class FavouriteRestaurants : Fragment(R.layout.fragment_fav_restaurant) {
    private lateinit var binding:FragmentFavRestaurantBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentFavRestaurantBinding.bind(view)

        val list=   listOf(
            RestaurantObject(
                name = "First Restaurant",
                rating = 1,
                cost = 200,
                isFavourite = true,
                id = 1
            ),
            RestaurantObject(
                name = "Second Restaurant",
                rating = 1,
                cost = 200,
                isFavourite = false,
                id = 2
            ),
            RestaurantObject(
                name = "Third Restaurant",
                rating = 1,
                cost = 200,
                isFavourite = true,
                id = 3
            ),
            RestaurantObject(
                name = "Fourth Restaurant",
                rating = 1,
                cost = 200,
                isFavourite = false,
                id = 4
            ),
            RestaurantObject(
                name = "Fifth Restaurant",
                rating = 1,
                cost = 200,
                isFavourite = true,
                id = 5
            )
        )
        val favRestaurantAdapter=RestaurantListAdapter()
        favRestaurantAdapter.submitList(list)
        binding.apply {
            favRestaurantList.apply {
                layoutManager=GridLayoutManager(requireContext(),2)
                setHasFixedSize(true)
                adapter=favRestaurantAdapter
            }
        }
//        homeAdapter.submitList(list)
    }

}