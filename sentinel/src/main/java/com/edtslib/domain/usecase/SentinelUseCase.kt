package com.edtslib.domain.usecase

import com.edtslib.domain.model.SentinelGroup
import kotlinx.coroutines.flow.Flow

interface SentinelUseCase {
    fun createSession(): Flow<String?>
    fun setUserId(userId: Long): Flow<Long>
    fun setLatLng(lat: Double?, lng: Double?): Flow<Boolean>

    fun track(
        group: String,
        name: String,
        details: Any? = null,
        userDetails: Any? = null
    ): Flow<Any?>
}