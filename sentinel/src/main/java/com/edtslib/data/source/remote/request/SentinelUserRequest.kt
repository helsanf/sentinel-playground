package com.edtslib.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class SentinelUserRequest (
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("user_name")
    val userName: String?
)