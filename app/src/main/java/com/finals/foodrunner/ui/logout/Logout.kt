package com.finals.foodrunner.ui.logout

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class Logout :DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Do you want to Logout")
            .setNegativeButton("No",null)
            .setPositiveButton("Yes",null).create()

    }
}