package com.edtslib.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class SentinelCoreRequest (
    @SerializedName("event_group")
    val eventAction: String,
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("source")
    val eventSource: String,
    @SerializedName("event_details")
    val details: Any?,
    @SerializedName("user_details")
    val userDetails: Any?
)
