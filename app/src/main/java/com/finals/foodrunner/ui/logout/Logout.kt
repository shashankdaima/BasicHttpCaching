package com.finals.foodrunner.ui.logout

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.util.PREF_NAME
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController


class Logout : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Do you want to Logout")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                clearCache()
                clearDatabase()
                findNavController().navigate(LogoutDirections.actionLogoutToLoginFragment2())
                Toast.makeText(requireContext(),"Signed Out Successfully",Toast.LENGTH_SHORT).show()

            }.create()

    }

    private  fun clearDatabase() {
        lifecycleScope.launchWhenStarted{
            val database = Room.databaseBuilder(
                requireContext().applicationContext,
                RestaurantDatabase::class.java,
                "restaurant_database"
            ).fallbackToDestructiveMigration()
                .build()
            val dao = database.restaurantDao()
            dao.deleteAllRestaurants()
        }

    }
    private fun clearCache(){
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear().apply()
        val ans=0;
    }


}