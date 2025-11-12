package com.edtslib.data.source.local

import android.content.SharedPreferences
import com.edtslib.data.base.LocalDataSource
import com.edtslib.data.source.remote.request.SentinelRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception

class SentinelQueueLocalDataSource(sharedPreferences: SharedPreferences):
    LocalDataSource<List<SentinelRequest>?>(sharedPreferences) {
        val mutex = Mutex()

    override fun getKeyName(): String = "sentinelQueue"
    override fun getValue(json: String): List<SentinelRequest>? = Gson().fromJson(json, object : TypeToken<List<SentinelRequest>?>() {}.type)

    suspend fun add(request: SentinelRequest): List<SentinelRequest>? {
        mutex.withLock {
            try {
                val cached = getCached()
                val list = cached?.toMutableList() ?: mutableListOf()
                list.add(request)

                save(list)
                return list
            } catch (_: Exception) {
                // nothing to do
                return null
            }
        }
    }

    suspend fun add(request: List<SentinelRequest>): List<SentinelRequest>? {
        mutex.withLock {
            try {
                val cached = getCached()
                val list = cached?.toMutableList() ?: mutableListOf()
                list.addAll(request)

                save(list)
                return list
            } catch (_: Exception) {
                // nothing to do
                return null
            }
        }
    }
}