package com.edtslib.domain.usecase

import com.edtslib.domain.model.SentinelGroup
import com.edtslib.domain.repository.ISentinelRepository

class SentinelInteractor(private val repository: ISentinelRepository) : SentinelUseCase {
    override fun createSession() = repository.createSession()
    override fun setUserId(userId: Long) = repository.setUserId(userId)
    override fun setLatLng(lat: Double?, lng: Double?) = repository.setLatLng(lat, lng)
    override fun track(
        group: String,
        name: String,
        details: Any?,
        userDetails : Any?
    ) = repository.track(group, name, details,userDetails)
}