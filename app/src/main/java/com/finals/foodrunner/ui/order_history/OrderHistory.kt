package com.finals.foodrunner.ui.order_history

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.order_history_adapter.OrderedFoodAdapter
import com.finals.foodrunner.databinding.FragmentOrderHistoryBinding
import com.finals.foodrunner.ui.activity.ActivityViewModel
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.ui.activity.OrderHistoryViewModel
import com.finals.foodrunner.util.DialogInterface
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.showNoInternetDialog
import kotlin.system.exitProcess

class OrderHistory : Fragment(R.layout.fragment_order_history) {

    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var viewModel: OrderHistoryViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderHistoryBinding.bind(view)
        val swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                viewModel.fetchOrderHistory()

            }
        }
        val adapter = OrderedFoodAdapter()
        binding.orderHistoryList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        lifecycleScope.launchWhenStarted {
            viewModel = (activity as MainActivity).viewModel
            viewModel.getOrderHistory().observe(viewLifecycleOwner, {

                adapter.submitList(it)
            })

            viewModel.getEvents().observe(viewLifecycleOwner, {
                when (it) {
                    ActivityViewModel.Event.LOADED -> {
                        Handler().postDelayed({
                            swipeRefreshLayout.isRefreshing = false
                        }, 200)
                    }
                    ActivityViewModel.Event.LOADING -> {
                        swipeRefreshLayout.isRefreshing = true

                    }
                    ActivityViewModel.Event.OFFLINE -> {
                        swipeRefreshLayout.isRefreshing = false
                        showNoInternetDialog(requireContext(), object : DialogInterface {
                            override fun onReload() {
                                lifecycleScope.launchWhenStarted {
                                    viewModel.fetchOrderHistory()

                                }

                            }

                            override fun onExitApp() {
                                activity?.finishAndRemoveTask();
                                exitProcess(0)
                            }

                        })

                    }
                }.exhaustive
            })
        }
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

}