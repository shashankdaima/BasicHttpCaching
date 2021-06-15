package com.finals.foodrunner.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.finals.foodrunner.R
import com.finals.foodrunner.controller.AppBarController
import com.finals.foodrunner.controller.DrawerController
import com.finals.foodrunner.controller.SearchViewController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), DrawerController, AppBarController, SearchViewController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

        }
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

            if (destination.id == R.id.nav_home || destination.id == R.id.nav_fav_restuarant) {
                fab.visibility = View.VISIBLE

            } else {
                fab.visibility = View.INVISIBLE
            }
            if (!(destination.id in appBarConfiguration.topLevelDestinations)) {
                hideAppBar()
                lockDrawer()
            } else {
                showAppBar()
                unlockDrawer()
            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
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
