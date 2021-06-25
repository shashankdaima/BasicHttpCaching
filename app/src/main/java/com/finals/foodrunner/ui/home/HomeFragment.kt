package com.finals.foodrunner.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentHomeBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.ui.activity.ActivityViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.exhaustive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ActivityViewModel
    private lateinit var searchView: SearchView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

        swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch{ viewModel.fetchAllRestaurants() }
        }
        viewModel.homeEvents.observe(viewLifecycleOwner, {
            when (it) {
                ActivityViewModel.Event.LOADED -> {
                    Handler().postDelayed({ swipeToRefresh.isRefreshing = false },2000)
                }
                ActivityViewModel.Event.LOADING -> swipeToRefresh.isRefreshing = true
                ActivityViewModel.Event.OFFLINE -> {
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("No Internet Available")
                        .setMessage("Please check internet connection.")
                        .setNegativeButton("QUIT") { _, _ ->
                            activity?.finishAndRemoveTask();
                            exitProcess(0)
                        }
                        .setPositiveButton("RELOAD") { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch{ viewModel.fetchAllRestaurants() }

                        }.create().show()

                    swipeToRefresh.isRefreshing = false
                }
            }.exhaustive
        })

        val adapter = RestaurantListAdapter(object : OnItemListener {
            override fun onItemClickListener(restaurant: Restaurant) {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToMenuFragment(restaurant))
            }

            override fun onItemFavClickListener(restaurant: Restaurant) {
                viewModel.changeFavouriteStatus(restaurant)

            }

        })
        viewModel.getRestaurants().observe(viewLifecycleOwner, {
            adapter.submitList(it)

        })
        binding.listAllRestaurants.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }

        setHasOptionsMenu(true)


    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            AlertDialog.Builder(requireContext()).setSingleChoiceItems(
                arrayOf(
                    "Sort by Rating",
                    "Sort by Increasing Cost",
                    "Sort by Decreasing Cost"
                ), 0
            ) { _, which ->
                if (which != -1) {

                }
            }

        }
        return super.onOptionsItemSelected(item)
    }


}
