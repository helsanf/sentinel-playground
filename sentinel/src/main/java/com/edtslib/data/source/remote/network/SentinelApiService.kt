package com.edtslib.data.source.remote.network

import com.edtslib.data.source.remote.request.SentinelRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SentinelApiService {
    @POST("api/v1/tracker/event")
    suspend fun send(@Body data: List<SentinelRequest>?): Response<Any?>
}