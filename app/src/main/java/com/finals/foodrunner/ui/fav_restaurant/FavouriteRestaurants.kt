package com.finals.foodrunner.ui.fav_restaurant

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentFavRestaurantBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.ui.activity.FavouriteViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.SORT_SCHEME
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar

class FavouriteRestaurants : Fragment(R.layout.fragment_fav_restaurant) {
    private lateinit var binding: FragmentFavRestaurantBinding
    private lateinit var viewModel:FavouriteViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavRestaurantBinding.bind(view)


        lifecycleScope.launchWhenStarted {
             viewModel = (activity as MainActivity).viewModel
            val favRestaurantAdapter = RestaurantListAdapter(object : OnItemListener {
                override fun onItemClickListener(restaurant: Restaurant) {
                    findNavController().navigate(
                        FavouriteRestaurantsDirections.actionNavFavRestuarantToMenuFragment(
                            restaurant
                        )
                    )
                }

                override fun onItemFavClickListener(restaurant: Restaurant) {
                    viewModel.unFavStatus(restaurant);
                    Snackbar.make(
                        requireView(),
                        "${restaurant.name} is unfavourited.",
                        Snackbar.LENGTH_LONG
                    ).setAction("UNDO") {
                        viewModel.unFavStatus(restaurant);

                    }.show()
                }

            })
            viewModel.getFavRestaurants().observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) {
                    binding.apply {
                        isEmptyPhoto.visibility = View.VISIBLE
                        favRestaurantList.visibility = View.INVISIBLE

                    }
                    Snackbar.make(
                        requireView(),
                        "It seems you haven't put anything as Favourite.",
                        Snackbar.LENGTH_LONG
                    ).show()

                } else {
                    binding.apply {
                        isEmptyPhoto.visibility = View.INVISIBLE
                        favRestaurantList.visibility = View.VISIBLE
                        favRestaurantAdapter.submitList(it)

                    }
                }
            })
            binding.favRestaurantList.apply {
                adapter = favRestaurantAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
                setHasFixedSize(true)
            }

        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView=searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            lifecycleScope.launchWhenStarted{ viewModel.setFavSearchQuery(it) }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            val prevSortScheme=when(viewModel.getFavSortOrder()){
                SORT_SCHEME.SORT_BY_RATING-> 0
                SORT_SCHEME.SORT_BY_INC_COST-> 1
                SORT_SCHEME.SORT_BY_DES_COST-> 2
                else   ->0

            }
            AlertDialog.Builder(requireContext()).setTitle("Choose sort order").setSingleChoiceItems(
                arrayOf(
                    "Sort by Rating",
                    "Sort by Increasing Cost",
                    "Sort by Decreasing Cost"
                ),prevSortScheme
            ) { _, which ->
                if (which != -1) {
                    when(which){
                        0->{
                            viewModel.setFavSortOrder(SORT_SCHEME.SORT_BY_RATING)
                        }
                        1->{
                            viewModel.setFavSortOrder(SORT_SCHEME.SORT_BY_INC_COST)


                        }
                        2->{
                            viewModel.setFavSortOrder(SORT_SCHEME.SORT_BY_DES_COST)


                        }
                        else->{
                            viewModel.setFavSortOrder(SORT_SCHEME.SORT_BY_RATING)

                        }

                    }.exhaustive

                }
            }.setPositiveButton("Close",null) .create().show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}