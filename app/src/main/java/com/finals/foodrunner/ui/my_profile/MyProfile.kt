package com.finals.foodrunner.ui.my_profile

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.FragmentMyProfileBinding
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.ui.activity.ProfileViewModel

class MyProfile : Fragment(R.layout.fragment_my_profile) {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyProfileBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            viewModel = (activity as MainActivity).viewModel
            viewModel.currentUser().observe(viewLifecycleOwner, {
                binding.apply {
                    name.setText(it.name);
                    address.setText(it.address);
                    phoneNumber.setText(it.mobile_number.toString());
                    email.setText(it.email);

                }
            })
        }



        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

}