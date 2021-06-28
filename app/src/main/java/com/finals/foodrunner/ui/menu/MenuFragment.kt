package com.finals.foodrunner.ui.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.MenuListAdapter
import com.finals.foodrunner.databinding.RestaurantMenuLayoutBinding
import com.finals.foodrunner.objects.MenuItem
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.DialogInterface
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.showNoInternetDialog
import com.finals.foodrunner.volley.VolleySingleton
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.system.exitProcess

class MenuFragment : Fragment(R.layout.restaurant_menu_layout) {


    private lateinit var viewModel: MenuViewModel
    private val args: MenuFragmentArgs by navArgs()
    private lateinit var binding: RestaurantMenuLayoutBinding
    private val TAG = "MenuFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RestaurantMenuLayoutBinding.bind(view)
        val progressBar = binding.progressBar
        val viewModelProvider = MenuViewModelProvider(
            VolleySingleton(requireContext()),
            ConnectivityManager(requireContext()),
            args.Restaurants
        )
        viewModel = ViewModelProvider(this, viewModelProvider).get(MenuViewModel::class.java)

        val adapter = MenuListAdapter(object : MenuListAdapter.OnItemClickListener {
            override fun onItemClick(menuItem: MenuItem) {
                lifecycleScope.launchWhenStarted { viewModel.updateSelectionStatus(menuItem) }
            }
        })

        binding.menuList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.addToCartButton.setOnClickListener {
            if (viewModel.getOrderedFood().isEmpty()) {
                Toast.makeText(requireContext(), "Please order something first", Toast.LENGTH_SHORT)
                    .show()
            } else {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToCartFragment(
                        viewModel.getOrderedFood(),
                        args.Restaurants
                    )
                )
            }

        }
        viewModel.apply {


            menu.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
            eventChannel.receiveAsFlow().asLiveData().observe(viewLifecycleOwner, {
                when (it) {
                    is MenuViewModel.Event.Error -> {
                        progressBar.visibility = View.INVISIBLE


                    }
                    is MenuViewModel.Event.LoadingComplete -> {
                        progressBar.visibility = View.INVISIBLE

                    }
                    is MenuViewModel.Event.LoadingStart -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    is MenuViewModel.Event.Offline -> {

                        showNoInternetDialog(requireContext(), object : DialogInterface {
                            override fun onReload() {
                                lifecycleScope.launchWhenStarted {
                                    viewModel.getMenu(
                                        restaurants
                                    )
                                }
                            }

                            override fun onExitApp() {
                                activity?.finishAndRemoveTask()
                                exitProcess(0)
                            }

                        })
                    }
                }.exhaustive
            })
        }


    }

}