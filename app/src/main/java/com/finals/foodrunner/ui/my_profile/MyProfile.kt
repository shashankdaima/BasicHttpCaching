package com.finals.foodrunner.ui.my_profile

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.FragmentMyProfileBinding
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.*

class MyProfile:Fragment(R.layout.fragment_my_profile){
    private lateinit var binding:FragmentMyProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentMyProfileBinding.bind(view)
        val sharedPreferences=requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        binding.name.text=sharedPreferences.getString(USER_NAME_KEY,"No User")
        binding.phoneNumber.text=sharedPreferences.getLong(USER_MOBILE_NUMBER_KEY,84400000000).toString()
        binding.email.text=sharedPreferences.getString(USER_EMAIL_KEY,"no_user@gmail.com")
        binding.address.text=sharedPreferences.getString(USER_ADDRESS_KEY,"No Address")


        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

}