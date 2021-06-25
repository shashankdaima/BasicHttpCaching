package com.finals.foodrunner.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.LoginFragmentBinding
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.util.*
import com.finals.foodrunner.volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(R.layout.login_fragment) {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel
    lateinit var sharedpreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)
        val viewModelFactory =
            LoginViewModelFactory(
                VolleySingleton(requireContext()),
                ConnectivityManager(requireContext())
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

       sharedpreferences =requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        viewModel.eventChannel.observe(viewLifecycleOwner, {
            when (it) {
                is LoginViewModel.Event.Success -> {
                    try {
                        sharedpreferences.edit().apply {
                            putLong(USER_ID_KEY, it.user.user_id)
                            putString(USER_NAME_KEY,it.user.name)
                            putLong(USER_MOBILE_NUMBER_KEY,it.user.mobile_number)
                            putString(USER_ADDRESS_KEY,it.user.address)
                            putString(USER_EMAIL_KEY,it.user.email)
                            putBoolean(USER_LOGGEDIN_KEY,true)
                        }.apply()

                        findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToNavHome())

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            e.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is LoginViewModel.Event.Error -> {
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()

                }
            }
        })
        binding.apply {
            moveToSignup.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignUpFragment())
            }

            loginMobileNo.apply {
                setText(viewModel.mobileLogin.value)
                addTextChangedListener {
                    viewModel.saveMobile(it.toString())
                }
            }
            loginPassword.apply {
                setText(viewModel.passwordLogin.value)
                addTextChangedListener {
                    viewModel.savePassword(it.toString())
                }
            }
            signinButton.setOnClickListener {
                viewModel.login()
            }

        }


    }
}