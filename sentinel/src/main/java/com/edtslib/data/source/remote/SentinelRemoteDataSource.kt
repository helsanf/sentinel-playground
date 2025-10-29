package com.edtslib.data.source.remote

import com.edtslib.data.base.BaseDataSource
import com.edtslib.data.source.remote.network.SentinelApiService
import com.edtslib.data.source.remote.request.SentinelRequest

class SentinelRemoteDataSource(
    private val apiService: SentinelApiService
) : BaseDataSource() {

    suspend fun send(data: List<SentinelRequest>?) =
        getResult {
            apiService.send(data)
        }

}