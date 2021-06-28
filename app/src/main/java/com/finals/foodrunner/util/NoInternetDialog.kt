package com.finals.foodrunner.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showNoInternetDialog(context: Context, dialogInterface: DialogInterface) {
    AlertDialog.Builder(context)
        .setTitle("No Internet Available")
        .setMessage("Please check internet connection.")
        .setNegativeButton("QUIT") { _, _ ->
            dialogInterface.onExitApp()
        }
        .setPositiveButton("RELOAD") { _, _ ->
            dialogInterface.onReload()
        }.create().show()

}

interface DialogInterface {
    fun onReload()
    fun onExitApp()
}