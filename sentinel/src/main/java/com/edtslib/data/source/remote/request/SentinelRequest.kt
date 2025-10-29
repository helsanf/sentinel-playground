package com.edtslib.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class SentinelRequest(
    @SerializedName("core")
    val core: SentinelCoreRequest,
    @SerializedName("user")
    val user: SentinelUserRequest?,
    @SerializedName("application")
    val application: SentinelApplicationRequest?,
    @SerializedName("network")
    val network: SentinelNetworkRequest?
)