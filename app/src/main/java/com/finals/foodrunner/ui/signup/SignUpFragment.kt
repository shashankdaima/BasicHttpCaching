package com.finals.foodrunner.ui.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.SignUpFragmentBinding

class SignUpFragment :Fragment(R.layout.sign_up_fragment){
    private lateinit var binding:SignUpFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= SignUpFragmentBinding.bind(view)

    }
}