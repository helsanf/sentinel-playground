package com.edtslib.domain.repository

import com.edtslib.domain.model.SentinelGroup
import kotlinx.coroutines.flow.Flow

interface ISentinelRepository {
    fun createSession(): Flow<String?>
    fun setUserId(userId: Long): Flow<Long>
    fun setLatLng(lat: Double?, lng: Double?): Flow<Boolean>

    fun track(
        group: String,
        name: String,
        details: Any? = null,
        userDetails : Any? = null
    ): Flow<Any?>

    fun submit(): Flow<Any?>
}