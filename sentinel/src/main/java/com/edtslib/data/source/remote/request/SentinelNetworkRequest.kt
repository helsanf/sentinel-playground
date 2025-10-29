package com.edtslib.data.source.remote.request

import android.annotation.SuppressLint
import com.edtslib.utils.ConnectivityUtil
import com.google.gson.annotations.SerializedName

data class SentinelNetworkRequest (
    @SerializedName("ip_address")
    val ipAddress: String?,
    @SerializedName("network_isp")
    val networkIsp: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("zipcode")
    val zipcode: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?

) {
    companion object {
        @SuppressLint("HardwareIds")
        fun create(latitude: Double?, longitude: Double?) : SentinelNetworkRequest {
            return SentinelNetworkRequest(ipAddress = ConnectivityUtil.getIPAddress(true),
                latitude = latitude,
                longitude = longitude,
                zipcode = "",
                city = "",
                country = "",
                networkIsp = "")
        }
    }
}