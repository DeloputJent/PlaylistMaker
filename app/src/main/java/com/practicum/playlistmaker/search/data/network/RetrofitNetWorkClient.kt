package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetWorkClient(private val iTunesService:iTunesSearchAPI,
                            private val context: Context,): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        } else {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesService.search(dto.expression)
                    //val body = resp.body() ?: Response()
                    resp.apply { resultCode = 200 }
                } catch (e: Exception) {
                    Response().apply { resultCode = 500 }
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

