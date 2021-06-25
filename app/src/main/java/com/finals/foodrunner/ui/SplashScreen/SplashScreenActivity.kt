package com.finals.foodrunner.ui.SplashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.finals.foodrunner.R
import com.finals.foodrunner.ui.activity.MainActivity
import com.finals.foodrunner.util.ConnectivityManager
import kotlin.system.exitProcess

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val connectivityManager = ConnectivityManager(this);
        if (connectivityManager.isOnline() && connectivityManager.checkConnectivity()) {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 500)
        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection Found")
                .setMessage("Please restart this app when Internet become available")
                .setPositiveButton("Okay"
                ) { _, _ ->
                    finishAndRemoveTask()
                    exitProcess(0);

                }.create().show()
        }


    }
}