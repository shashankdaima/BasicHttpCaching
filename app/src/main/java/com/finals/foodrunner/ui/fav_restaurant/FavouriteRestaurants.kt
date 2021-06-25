package com.finals.foodrunner.ui.fav_restaurant

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentFavRestaurantBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.ui.home.HomeFragmentDirections
import com.google.android.material.snackbar.Snackbar

class FavouriteRestaurants : Fragment(R.layout.fragment_fav_restaurant) {
    private lateinit var binding: FragmentFavRestaurantBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavRestaurantBinding.bind(view)
        val viewModel=(activity as MainActivity).viewModel
        val favRestaurantAdapter = RestaurantListAdapter(object : OnItemListener {
            override fun onItemClickListener(restaurant: Restaurant) {
                findNavController().navigate(FavouriteRestaurantsDirections.actionNavFavRestuarantToMenuFragment(restaurant))


            }

            override fun onItemFavClickListener(restaurant: Restaurant) {
                viewModel.changeFavouriteStatus(restaurant);
                Snackbar.make(requireView(),"${restaurant.name} is unfavourited.",Snackbar.LENGTH_LONG).setAction("UNDO") {
                    viewModel.changeFavouriteStatus(restaurant);

                }.show()
            }

        })
        viewModel.favouriteRestaurants.observe(viewLifecycleOwner,{
            if(it.isNullOrEmpty()){
                binding.apply {
                    isEmptyPhoto.visibility=View.VISIBLE
                    favRestaurantList.visibility=View.INVISIBLE

                }
                Snackbar.make(requireView(),"It seems you have put anything as Favourite.",Snackbar.LENGTH_LONG).show()

            }
            else{
                binding.apply {
                    isEmptyPhoto.visibility=View.INVISIBLE
                    favRestaurantList.visibility=View.VISIBLE
                    favRestaurantAdapter.submitList(it)

                }
            }
        })
        binding.favRestaurantList.apply {
            adapter=favRestaurantAdapter
            layoutManager=GridLayoutManager(requireContext(),2)
            setHasFixedSize(true)
        }

    }


}