package com.adsum.camel_masterapplication.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.adsum.camel_masterapplication.utils.App


class ConnectivityReceiver : BroadcastReceiver() {
    private var connectivityReceiverListener: ConnectivityReceiverListener? =
        null


    override fun onReceive(context: Context?, intent: Intent?) {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        connectivityReceiverListener?.onNetworkConnectionChanged(isConnected)

    }

    companion object{
        fun isConnected(): Boolean {
            val cm = App.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            @SuppressLint("MissingPermission") val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }


    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}