
package com.henryudorji.theater.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Network Manager, extends capabilities of  ConnectivityManager#NetworkCallback()
 * by providing a observable callback on network status
 *
 * Author : [https://github.com/ch8n]
 * website : [https://chetangupta.net]
 * Creation Date : 4-08-2020
 */
class NetworkManager(context: Context) : ConnectivityManager.NetworkCallback() {

    private val _connectionStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val observeConnectionStatus: LiveData<Boolean> = _connectionStatusLiveData

    private val appContext: Context = context.applicationContext

    init {
        val connectivityManager = appContext.getSystemService<ConnectivityManager>()

        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallbackCompact(this)

            val connectionStatus = connectivityManager.allNetworks.any { network ->
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return@any networkCapabilities?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) == true
            }

            _connectionStatusLiveData.value = connectionStatus
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _connectionStatusLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _connectionStatusLiveData.postValue(false)
    }

    private fun ConnectivityManager.registerNetworkCallbackCompact(networkManager: NetworkManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerDefaultNetworkCallback(networkManager)
        } else {
            val builder = NetworkRequest.Builder()
            registerNetworkCallback(builder.build(), networkManager)
        }
    }
}
