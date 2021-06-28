package com.finals.foodrunner.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.OnItemListener
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentHomeBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.objects.User
import com.finals.foodrunner.ui.activity.ActivityViewModel
import com.finals.foodrunner.ui.activity.HomeViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlin.system.exitProcess

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchView: SearchView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private val args: HomeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginCheckAndUserUpdate()
        binding = FragmentHomeBinding.bind(view)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launchWhenStarted { viewModel.fetchAllRestaurants() }
        }
        val adapter = RestaurantListAdapter(object : OnItemListener {
            override fun onItemClickListener(restaurant: Restaurant) {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavHomeToMenuFragment(
                        restaurant
                    )
                )
            }

            override fun onItemFavClickListener(restaurant: Restaurant) {
                viewModel.changeFavouriteStatus(restaurant)

            }

        })

        lifecycleScope.launchWhenStarted {
            viewModel.getHomeEvents().collect {

                when (it) {
                    ActivityViewModel.Event.LOADED -> {
                        delay(2000)
                        swipeToRefresh.isRefreshing = false
                    }
                    ActivityViewModel.Event.LOADING -> swipeToRefresh.isRefreshing = true
                    ActivityViewModel.Event.OFFLINE -> {
                        showNoInternetDialog(requireContext(), object : DialogInterface {
                            override fun onReload() {
                                lifecycleScope.launchWhenStarted { viewModel.fetchAllRestaurants() }
                            }

                            override fun onExitApp() {
                                activity?.finishAndRemoveTask();
                                exitProcess(0)
                            }

                        })

                        swipeToRefresh.isRefreshing = false
                    }
                }.exhaustive
            }
        }
        viewModel.getRestaurants().observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                lifecycleScope.launchWhenStarted { viewModel.fetchAllRestaurants() }
            }
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
        searchView.onQueryTextChanged {
            viewModel.setHomeSearchQuery(it)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            val prevSortScheme=when(viewModel.getHomeSortOrder()){
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
                            viewModel.setHomeSortOrder(SORT_SCHEME.SORT_BY_RATING)
                        }
                        1->{
                            viewModel.setHomeSortOrder(SORT_SCHEME.SORT_BY_INC_COST)


                        }
                        2->{
                            viewModel.setHomeSortOrder(SORT_SCHEME.SORT_BY_DES_COST)


                        }
                        else->{
                            viewModel.setHomeSortOrder(SORT_SCHEME.SORT_BY_RATING)

                        }

                    }.exhaustive

                }
            }.setPositiveButton("Close",null) .create().show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loginCheckAndUserUpdate() {
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (!preferences.getBoolean(USER_LOGGEDIN_KEY, false) && args.user == null) {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToLoginFragment2())
        } else if (args.user == null) {
            lifecycleScope.launchWhenStarted {
                viewModel.setCurrentUser(
                    User(
                        name = preferences.getString(USER_NAME_KEY, ""),
                        email = preferences.getString(USER_EMAIL_KEY, ""),
                        mobile_number = preferences.getLong(USER_MOBILE_NUMBER_KEY, 0),
                        user_id = preferences.getLong(USER_ID_KEY, 0),
                        address = preferences.getString(USER_ADDRESS_KEY, "")
                    )
                )
            }
        } else {
            lifecycleScope.launchWhenStarted {
                viewModel.setCurrentUser(args.user!!)
            }
        }
    }

}
