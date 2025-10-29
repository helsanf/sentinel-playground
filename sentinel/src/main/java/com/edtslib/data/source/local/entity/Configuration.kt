package com.edtslib.data.source.local.entity

data class Configuration (
    var sessionId: String,
    var userId: Long,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var eventId: Long = 1,
    var flushTime: Long = 0
)