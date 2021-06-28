package com.finals.foodrunner.ui.confirmation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.ConfirmationFragmentBinding
import com.finals.foodrunner.util.*
import com.finals.foodrunner.volley.VolleySingleton
import kotlin.system.exitProcess

class ConfirmationFragment : Fragment(R.layout.confirmation_fragment) {
    private lateinit var binding: ConfirmationFragmentBinding
    private lateinit var viewModel: ConfirmationViewModel
    private val args: ConfirmationFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConfirmationFragmentBinding.bind(view)
        val volleySingleton = VolleySingleton(requireContext())
        val connectivityManager = ConnectivityManager(requireContext())
        val sharedPreferences =
            requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val cartViewModelFactory =
            ConfirmationViewModelFactory(
                volleySingleton,
                connectivityManager,
                args.orderedFood,
                userId = sharedPreferences.getLong(
                    USER_ID_KEY, 0
                )
            )
        viewModel =
            ViewModelProvider(this, cartViewModelFactory).get(ConfirmationViewModel::class.java)
        binding.button.setOnClickListener {
            findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragment2ToNavHome())
        }
        viewModel.events.observe(viewLifecycleOwner, {
            when (it) {
                ConfirmationViewModel.Event.LOADING -> {
                    binding.apply {
                        myProgressBar.visibility = View.VISIBLE
                        success.visibility = View.INVISIBLE
                        error.visibility = View.INVISIBLE


                    }

                }
                ConfirmationViewModel.Event.SUCCESS -> {
                    binding.apply {
                        myProgressBar.visibility = View.INVISIBLE
                        success.visibility = View.VISIBLE
                        error.visibility = View.INVISIBLE
                        button.visibility = View.VISIBLE
                    }


                }
                ConfirmationViewModel.Event.FAILURE -> {
                    binding.apply {
                        myProgressBar.visibility = View.INVISIBLE
                        success.visibility = View.INVISIBLE
                        error.visibility = View.VISIBLE
                        button.visibility = View.VISIBLE

                    }

                }
                ConfirmationViewModel.Event.OFFLINE -> {
                    showNoInternetDialog(requireContext(), object : DialogInterface {
                        override fun onReload() {
                            lifecycleScope.launchWhenStarted { viewModel.orderFood() }
                        }

                        override fun onExitApp() {
                            activity?.finishAndRemoveTask()
                            exitProcess(0)
                        }

                    })
                    binding.apply {
                        myProgressBar.visibility = View.INVISIBLE
                        success.visibility = View.INVISIBLE
                        error.visibility = View.INVISIBLE
                    }
                }
            }.exhaustive
        })


    }
}