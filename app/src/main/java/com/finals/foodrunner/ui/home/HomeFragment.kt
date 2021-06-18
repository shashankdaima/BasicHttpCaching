package com.finals.foodrunner.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentHomeBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.room.SORT_MAP
import com.finals.foodrunner.room.SORT_SCHEME
import com.finals.foodrunner.ui.activity.HomeViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.ui.activity.MainActivityViewModel
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchView: SearchView
    private lateinit var eventChannel: Channel<MainActivityViewModel.Events>
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            viewModel.updateAllRestaurants(true)
        }
        lifecycleScope.launchWhenStarted{
            viewModel.fetchFromDatabase()?.collect {
                binding.listAllRestaurants.apply {
                    val adapter = RestaurantListAdapter(object : OnItemListener {
                        override fun onItemClickListener(restaurant: Restaurant) {
                            findNavController().navigate(HomeFragmentDirections.actionNavHomeToMenuFragment(restaurant))
                        }

                        override fun onItemFavClickListener(restaurant: Restaurant) {
                            viewModel.restaurantFavChanged(restaurant)
                        }

                    })
                    adapter.submitList(it)
                    this.adapter = adapter
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    setHasFixedSize(false)
                }
            }
        }
        eventChannel = viewModel.getHomeEventChannel()
        CoroutineScope(Dispatchers.IO).launch {
            eventChannel.receiveAsFlow().collect {
                when (it) {
                    is MainActivityViewModel.Events.LoadingComplete-> {
                        delay(TimeUnit.SECONDS.toMillis(1))
                        swipeToRefresh.isRefreshing=false

                    }
                    is MainActivityViewModel.Events.LoadingStart-> swipeToRefresh.isRefreshing=true
                    is MainActivityViewModel.Events.ErrorHomeFragment->{
                        Snackbar.make(requireView(),it.error.localizedMessage!!,Snackbar.LENGTH_SHORT).show()
                    }

                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.getQuery().value
        if (pendingQuery != null) {
            if (pendingQuery.isNotEmpty()) {
                searchItem.expandActionView()
                searchView.setQuery(pendingQuery, false)
            }
        }

        searchView.onQueryTextChanged {

            viewModel.setQuery(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {

            SORT_MAP_INDEX.get(viewModel.getSortOrderLiveData().value)?.let {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Sort By")
                    .setSingleChoiceItems(
                        SORT_MAP.values.toTypedArray(), it
                    ) { _, which ->
                        viewModel.sortOrderChanged(index = which)
                    }.setPositiveButton("Close", null)
                    .create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val SORT_MAP_INDEX = mapOf<SORT_SCHEME, Int>(
        SORT_SCHEME.SORT_BY_RATING to 0,
        SORT_SCHEME.SORT_BY_INC_COST to 1,
        SORT_SCHEME.SORT_BY_DES_COST to 2
    ).exhaustive

}
