package com.finals.foodrunner.ui.forgot_password.password_change

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.PasswordChangeFragmentBinding
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.DialogInterface
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.util.showNoInternetDialog
import com.finals.foodrunner.volley.VolleySingleton
import kotlin.system.exitProcess

class PasswordSaveFragment : Fragment(R.layout.password_change_fragment) {
    private lateinit var binding: PasswordChangeFragmentBinding
    private lateinit var viewModel: PasswordSaveViewModel
    private val args: PasswordSaveFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PasswordChangeFragmentBinding.bind(view)
        val factory = PasswordSaveViewModelFactory(
            volleySingleton = VolleySingleton(requireContext()),
            connectivityManager = ConnectivityManager(requireContext())
        )
        this.viewModel = ViewModelProvider(this, factory).get(PasswordSaveViewModel::class.java)
        viewModel.eventChannel.observe(viewLifecycleOwner, {
            when (it) {

                is PasswordSaveViewModel.Event.NoInternet -> {
                    showNoInternetDialog(requireContext(), object : DialogInterface {
                        override fun onReload() {
                            if (viewModel.password.value != viewModel.confirm_password.value) {
                                Toast.makeText(
                                    requireContext(),
                                    "Password Mismatch",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                viewModel.verifyAndChangePassword(args.mobileNumber)
                            }

                        }

                        override fun onExitApp() {
                            activity?.finishAndRemoveTask()
                            exitProcess(0)
                        }

                    })
                }
                is PasswordSaveViewModel.Event.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                }
                is PasswordSaveViewModel.Event.Success -> {
                    AlertDialog.Builder(requireContext()).setTitle("Congratulations")
                        .setMessage(it.message + " Please proceed to Login Screen")
                        .setPositiveButton("Ok") { _, _ ->

                        }.create().show()

                }
            }.exhaustive
        })
        binding.apply {
            otp.setText(viewModel.otp.value)
            password.setText(viewModel.password.value)
            confirmPassword.setText(viewModel.confirm_password.value)
            otp.addTextChangedListener {
                viewModel.saveOtp(it.toString())
            }
            password.addTextChangedListener {
                viewModel.savePassword(it.toString())
            }
            confirmPassword.addTextChangedListener {
                viewModel.saveConfirmPassword(it.toString())
            }
            changePasswordButton.setOnClickListener {
                if (viewModel.password.value != viewModel.confirm_password.value) {
                    Toast.makeText(requireContext(), "Password Mismatch", Toast.LENGTH_SHORT).show()

                } else {
                    viewModel.verifyAndChangePassword(args.mobileNumber)
                }

            }
        }

    }


}