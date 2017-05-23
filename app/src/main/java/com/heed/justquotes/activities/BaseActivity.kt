package com.heed.justquotes.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Howard.
 */
open class BaseActivity : AppCompatActivity() {

    /**
     * @return A check to see if device is currently connected to the internet
     */
    var isConnectedToInternet = false

    private val urlConnection: HttpURLConnection? = null
    private var internetConnectionListener: InternetConnectionListener? = null
    private val mConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isConnectedToInternet = false
            val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            val isNetworkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected

            if (isNetworkConnected) { // Does not mean connected to internet, just that connected to some connection
                val netCheckAsyncTask = NetCheckAsyncTask(urlConnection, isConnectedToInternet, internetConnectionListener)
                netCheckAsyncTask.execute()
            } else {
                if (internetConnectionListener != null)
                    internetConnectionListener!!.onInternetConnectionStatusChanged(isConnectedToInternet)
                Log.d(TAG, "No network available!")
            }
        }
    }

    private class NetCheckAsyncTask(private var urlConnection: HttpURLConnection?, private var isConnectedToInternet: Boolean, private val internetConnectionListener: InternetConnectionListener?) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            // Give this a timeout
            try {
                urlConnection = URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
                urlConnection!!.setRequestProperty("User-Agent", "Android")
                urlConnection!!.setRequestProperty("Connection", "close")
                urlConnection!!.connectTimeout = 1500
                urlConnection!!.connect()
                isConnectedToInternet = urlConnection!!.responseCode == 204 && urlConnection!!.contentLength == 0
                Log.d(TAG, "isConnectedToInternet:" + isConnectedToInternet)
            } catch (e: IOException) {
                Log.e(TAG, "Error checking internet connection:" + e.message, e)
            }

            return null
        }

        override fun onPostExecute(aVoid: Void) {
            internetConnectionListener?.onInternetConnectionStatusChanged(isConnectedToInternet)
            if (urlConnection != null) urlConnection!!.disconnect()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * To register the receiver to retrieve internet connection status when needed
     * **NB:** Do not use at the start an activity/or fragment that loads attached to activity and
     * expect an immediate response on connection status (due to AsyncTask delay).
     *
     * In those instances, do the check manually like so to handle the onPostExecute yourself:
     * new AsyncTask<Void></Void>, Void, Void>() {
     * boolean isConnected = false;
     *
     * protected Void doInBackground(Void... params) {
     * isConnected = ConnectionHelper.hasInternetAccess(SplashActivity.this);
     * return null;
     * }
     *
     * protected void onPostExecute(Void aVoid) {
     * ConnectionHelper.disconnectConnection();
     * MyActivity.this.isConnected = isConnected;
     * doFollowUpMethod();
     * }
     * }.execute();
     */
    fun registerInternetCheckReceiver(internetConnectionListener: InternetConnectionListener) {
        this.internetConnectionListener = internetConnectionListener
        registerReceiver(mConnectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    /**
     * To unregister the receiver retrieving the internet connection status when not needed
     */
    fun unregisterInternetCheckReceiver() {
        try {
            unregisterReceiver(mConnectionReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.message, e)
        }
    }

    /**
     * To display simple re-usable Snackbar with message and Close button
     *
     * @param container The container the Snackbar will be shown in
     *
     * @param message   The message to be displayed
     */
    fun basicSnackBarWClose(container: View, message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT)
                .setAction("Close") { _ -> }.show()
    }

    /**
     * To display simple re-usable Snackbar with message and Close button
     * @param container  The container the Snackbar will be shown in
     *
     * @param message    The message to be displayed
     *
     * @param lengthLong boolean indicating whether the Long length should be used for Snackbar or not
     */
    fun basicSnackBarWClose(container: View, message: String, lengthLong: Boolean) {
        Snackbar.make(container, message, if (lengthLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
                .setAction("Close") { _ -> }.show()
    }

    /**
     * To display simple re-usable Snackbar which cannot be closed until something external happens
     *
     * @param container The container the Snackbar will be shown in
     * *
     * @param message   The message to be displayed
     */
    fun basicIndefiniteSnackBar(container: View, message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_INDEFINITE).show()
    }

    /**
     * To show a simple indeterminate length progress dialog. It's view is controlled like so:
     *
     * MaterialDialog progressDialog = showIndeterminateProgressDialog("Doing something ...", true).build();
     * progressDialog.show();
     *
     * progressDialog.dismiss();
     * @param content    The string to be shown in the dialog to inform the user what is happening
     * *
     * @param horizontal If true, show horizontal slider, otherwise, show loading spinner
     * *
     * @return The built dialog
     */
    fun showIndeterminateProgressDialog(content: String, horizontal: Boolean): MaterialDialog.Builder {
        return MaterialDialog.Builder(this)
                .content(content).progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .canceledOnTouchOutside(false).cancelable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterInternetCheckReceiver()
    }

    /**
     * An interface getting the updated connection status
     */
    interface InternetConnectionListener {
        fun onInternetConnectionStatusChanged(isConnected: Boolean)
    }

    companion object {

        private val TAG = BaseActivity::class.java.simpleName
    }
}
