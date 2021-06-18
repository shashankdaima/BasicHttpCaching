package com.finals.foodrunner.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.finals.foodrunner.room.RestaurantDatabase
import com.finals.foodrunner.ui.menu.MenuFragment
import com.finals.foodrunner.util.ConnectivityManager
import com.finals.foodrunner.volley.RestaurantApi
import com.finals.foodrunner.volley.VolleySingleton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), DrawerController, AppBarController, SearchViewController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
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
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if(destination.id ==R.id.menuFragment){
                lockDrawer()
                showAppBar()


            }
            else if (!(destination.id in appBarConfiguration.topLevelDestinations)) {
                hideAppBar()
                lockDrawer()
            } else {
                showAppBar()
                unlockDrawer()
            }

        }
        val volleySingleton = VolleySingleton(this)
        val connectivityManager = ConnectivityManager(this)
        val viewModelFactory = MainActivityFactory(
            volleySingleton = volleySingleton,
            connectivityManager = connectivityManager,
            api = RestaurantApi(),
            restaurantDatabase = Room.databaseBuilder(
                applicationContext,
                RestaurantDatabase::class.java,
                "task_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        lifecycleScope.launchWhenStarted {
            viewModel.connectionChannel.receiveAsFlow().collect {
                if (it == MainActivityViewModel.ConnectivityCheck.OFFLINE) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("No Internet Found")
                        .setMessage("Please connect to Internet")
                        .setNegativeButton("Close") { _, _ ->
                            finishAffinity();
                            exitProcess(0);
                        }
                        .setPositiveButton("Reload") { _, _ ->
                            viewModel.reloadHome()
                        }
                        .create().show()

                }
            }
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
        toolbar.visibility = View.VISIBLE
    }

    override fun hideAppBar() {


        toolbar.visibility = View.GONE
    }
}
