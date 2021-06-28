package com.finals.foodrunner.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.SignUpFragmentBinding
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.exhaustive
import com.finals.foodrunner.volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.receiveAsFlow

class SignUpFragment : Fragment(R.layout.sign_up_fragment) {
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var viewModel: SignUpViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpFragmentBinding.bind(view)
        val volleySingleton = VolleySingleton(requireContext())
        val connectivityManager = ConnectivityManager(requireContext())
        val viewModelFactory = SignUpViewModelFactory(volleySingleton, connectivityManager)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)
        binding.apply {
            name.setText(viewModel.name.value)
            email.setText(viewModel.email.value)
            mobileNumber.setText(viewModel.mobileNumber.value)
            location.setText(viewModel.location.value)
            password.setText(viewModel.password.value)
            confirmPassword.setText(viewModel.confirmPassword.value)
            name.addTextChangedListener {
                viewModel.nameChanged(it.toString())
            }
            email.addTextChangedListener {
                viewModel.emailChanged(it.toString())
            }
            mobileNumber.addTextChangedListener {
                viewModel.mobileNumberChange(it.toString())
            }
            location.addTextChangedListener {
                viewModel.locationChanged(it.toString())
            }
            password.addTextChangedListener {
                viewModel.passwordChanged(it.toString())
            }
            confirmPassword.addTextChangedListener {
                viewModel.confirmPasswordChanged(it.toString())
            }
            register.setOnClickListener {
                viewModel.signUp()
            }
            moveToSignup.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment2())
            }

        }
        viewModel.eventChannel.receiveAsFlow().asLiveData().observe(viewLifecycleOwner, {
            when (it) {
                is SignUpViewModel.Event.Error -> {
                    Snackbar.make(requireView(), "${it.message}",Snackbar.LENGTH_LONG).setAction("Retry"){
                        viewModel.signUp()
                    }.show()
                }
                SignUpViewModel.Event.PasswordMismatch -> {
                    Toast.makeText(requireContext(), "Password Mismatch", Toast.LENGTH_SHORT).show()
                }
                is SignUpViewModel.Event.Success ->{
                    Snackbar.make(requireView(), "Hi ${it.user.name}!, your account is successfully registered. You can now log into your account",Snackbar.LENGTH_LONG).show()
                }
            }.exhaustive
        })

    }
}