package com.finals.foodrunner.util
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import java.io.IOException
import java.util.*

//https://stackoverflow.com/questions/53532406/activenetworkinfo-type-is-deprecated-in-api-level-28

class ConnectivityManager {
    fun checkConnectivity(context:Context):Boolean   {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            val result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result

    }
    fun isOnline(): Boolean {
        /*Just to check Time delay*/
        val t: Long = Calendar.getInstance().getTimeInMillis()
        val runtime = Runtime.getRuntime()
        try {
            /*Pinging to Google server*/
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            val t2: Long = Calendar.getInstance().getTimeInMillis()
            Log.i("NetWork check Time", (t2 - t).toString() + "")
        }
        return false
    }
}