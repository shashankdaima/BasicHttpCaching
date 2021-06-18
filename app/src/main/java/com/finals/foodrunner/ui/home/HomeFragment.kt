package com.finals.foodrunner.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentHomeBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.SORT_MAP
import com.finals.foodrunner.room.SORT_SCHEME
import com.finals.foodrunner.ui.activity.HomeViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.onQueryTextChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery =""
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {


            lifecycleScope.launch {

                delay(TimeUnit.SECONDS.toMillis(1 ))
                viewModel.fetchFromDatabase()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {

//            SORT_MAP_INDEX.get(viewModel.getSortOrderLiveData().value)?.let {
//                androidx.appcompat.app.AlertDialog.Builder(requireContext())
//                    .setTitle("Sort By")
//                    .setSingleChoiceItems(
//                        SORT_MAP.values.toTypedArray(), it
//                    ) { _, which ->
//                        viewModel.sortOrderChanged(index = which)
//                    }.setPositiveButton("Close", null)
//                    .create().show()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.fetchAllRestaurants().observe(viewLifecycleOwner, {
            binding.listAllRestaurants.apply {
                val adapter = RestaurantListAdapter(object :OnItemListener{
                    override fun onItemClickListener(restaurant: Restaurant) {

                    }

                    override fun onItemFavClickListener(restaurant: Restaurant) {
                        viewModel.restaurantFavChanged(restaurant)
                    }

                })
                adapter.submitList(it)
                this.adapter = adapter;
                layoutManager = GridLayoutManager(requireContext(), 2)
                setHasFixedSize(true)

            }
        })
        viewModel.getSortOrderLiveData().observe(viewLifecycleOwner) {
            viewModel.fetchFromDatabase()
        }

    }

    val SORT_MAP_INDEX = mapOf<SORT_SCHEME, Int>(
        SORT_SCHEME.SORT_BY_RATING to 0,
        SORT_SCHEME.SORT_BY_INC_COST to 1,
        SORT_SCHEME.SORT_BY_DES_COST to 2
    ).exhaustive

}
