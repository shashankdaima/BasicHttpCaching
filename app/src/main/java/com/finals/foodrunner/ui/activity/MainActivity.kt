package com.finals.foodrunner.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.finals.foodrunner.R
import com.finals.foodrunner.controller.AppBarController
import com.finals.foodrunner.controller.DrawerController
import com.finals.foodrunner.controller.SearchViewController
import com.finals.foodrunner.databinding.ActivityMainBinding
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.util.*
import com.finals.foodrunner.volley.VolleySingleton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView


class MainActivity : AppCompatActivity(), DrawerController, AppBarController, SearchViewController {

    lateinit var viewModel: ActivityViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FoodRunner)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_my_profile,
                R.id.nav_fav_restuarant,
                R.id.nav_order_history,
                R.id.nav_faq,
                R.id.logout
            ), drawerLayout
        )
        val volleySingleton = VolleySingleton(this)
        val connectivityManager = ConnectivityManager(this)
        val restaurantDatabase = Room.databaseBuilder(
            applicationContext,
            RestaurantDatabase::class.java,
            "restaurant_database"
        ).fallbackToDestructiveMigration()
            .build()


        val viewModelFactory =
            ActivityViewModelFactory(
                volleySingleton, connectivityManager, restaurantDatabase
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ActivityViewModel::class.java)

        setupCredentials()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.menuFragment || destination.id == R.id.cartFragment) {
                lockDrawer()
                showAppBar()


            } else if (!(destination.id in appBarConfiguration.topLevelDestinations)) {
                hideAppBar()
                lockDrawer()
            } else {
                showAppBar()
                unlockDrawer()
            }

        }


    }


    fun setupCredentials() {
        viewModel.user.observe(this){
            navView.getHeaderView(0)
                .findViewById<MaterialTextView>(R.id.user_name_nav_header).text =it.name
            navView.getHeaderView(0)
                .findViewById<MaterialTextView>(R.id.user_phone_number_nav_header).text =
                "+91-${it.mobile_number}"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }


    override fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun showAppBar() {
        supportActionBar?.show()
    }

    override fun hideAppBar() {

        supportActionBar?.hide()

    }

    fun setupCredentials(name: String?, mobileNumber: String) {
        navView.getHeaderView(0).findViewById<MaterialTextView>(R.id.user_name_nav_header).text =
            name
        navView.getHeaderView(0)
            .findViewById<MaterialTextView>(R.id.user_phone_number_nav_header).text =
            "+91-$mobileNumber"
    }
}
