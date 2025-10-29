package com.edtslib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.edtslib.domain.model.SentinelGroup
import com.edtslib.domain.usecase.SentinelUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SentinelViewModel(
    private val trackerUseCase: SentinelUseCase
) : ViewModel() {
    fun createSession() = trackerUseCase.createSession().asLiveData()

    fun setUserId(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setUserId(userId).collect()
        }
    }

    fun setLatLng(lat: Double?, lng: Double?) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setLatLng(lat, lng).collect()
        }
    }

    @OptIn(FlowPreview::class)
    fun trackClick(
        name: String,
        details: Any? = null,
        userDetails : Any? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                trackerUseCase.track(
                    group = SentinelGroup.Click.toString(),
                    name = name,
                    details = details,
                    userDetails = userDetails).collect {
                }
            }
            catch (_: Error) {

            }
        }
    }

    @OptIn(FlowPreview::class)
    fun track(
        group : String,
        name: String,
        details: Any? = null,
        userDetails : Any? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                trackerUseCase.track(
                    group = group,
                    name = name,
                    details = details,
                    userDetails = userDetails).collect {
                }
            }
            catch (_: Error) {

            }
        }
    }
}