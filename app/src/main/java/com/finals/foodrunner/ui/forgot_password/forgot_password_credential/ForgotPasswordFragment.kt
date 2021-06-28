package com.finals.foodrunner.ui.forgot_password.forgot_password_credential

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.ForgotPasswordFragmentBinding
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.DialogInterface
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.showNoInternetDialog
import com.finals.foodrunner.volley.VolleySingleton
import kotlin.system.exitProcess

class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {
    private lateinit var binding: ForgotPasswordFragmentBinding
    private lateinit var viewModel: ForgotPasswordViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForgotPasswordFragmentBinding.bind(view)
        val factory = ForgotPasswordViewModelFactory(
            volleySingleton = VolleySingleton(requireContext()),
            connectivityManager = ConnectivityManager(requireContext())
        )
        this.viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)
        viewModel.eventChannel.observe(viewLifecycleOwner, {
            when (it) {
                is ForgotPasswordViewModel.Event.Error -> {
                    Toast.makeText(requireContext(), "Error:" + it.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is ForgotPasswordViewModel.Event.Success -> {
                    AlertDialog.Builder(requireContext()).setTitle("Process Completed")
                        .setMessage("New Otp is send to your email. Please check it. It has 24-hrs validation.")
                        .setPositiveButton("Ok") { _, _ ->
                            sendUserToChangePasswordScreen()
                        }.create().show()
                }
                is ForgotPasswordViewModel.Event.SuccessAndSecondInvoke -> {
                    AlertDialog.Builder(requireContext()).setTitle("Process Completed")
                        .setMessage("It seems that you have already applied for otp. Please use the same otp")
                        .setPositiveButton("Ok") { _, _ ->
                            sendUserToChangePasswordScreen()

                        }.create().show()
                }
                is ForgotPasswordViewModel.Event.NoInternet -> {
                    showNoInternetDialog(requireContext(), object : DialogInterface {
                        override fun onReload() {
                            viewModel.submitCredentials()
                        }

                        override fun onExitApp() {
                            activity?.finishAndRemoveTask()
                            exitProcess(0)
                        }

                    })
                }
            }.exhaustive
        })
        binding.apply {
            mobileNumber.setText(viewModel.mobileNumber.value)
            email.setText(viewModel.email.value)
            mobileNumber.addTextChangedListener {
                viewModel.saveMobile(it.toString())
            }
            email.addTextChangedListener {
                viewModel.saveEmail(it.toString())
            }

            nextButton.setOnClickListener {
                viewModel.submitCredentials()
            }

        }

    }

    private fun sendUserToChangePasswordScreen() {
        viewModel.mobileNumber.value?.let {
            findNavController().navigate(
                ForgotPasswordFragmentDirections.actionForgotPasswordFragment2ToPasswordSaveFragment(
                    it
                )
            )
        }
    }
}