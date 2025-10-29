package com.edtslib.data.source.local

import android.content.SharedPreferences
import com.edtslib.data.base.LocalDataSource
import com.edtslib.data.source.local.entity.Configuration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConfigurationLocalSource(sharedPreferences: SharedPreferences) :
    LocalDataSource<Configuration>(sharedPreferences) {
    private var configuration: Configuration? = null

    override fun getKeyName(): String = "sentinelConfiguration"
    override fun getValue(json: String): Configuration =
        Gson().fromJson(json, object : TypeToken<Configuration>() {}.type)

    override fun save(data: Configuration?) {
        configuration = data
        super.save(data)
    }

    override fun getCached(): Configuration? {
        return if (configuration != null) {
            configuration
        } else {
            super.getCached()
        }
    }

    override fun clear() {
        configuration = null
        super.clear()
    }

    fun getSessionId() = getCached()?.sessionId
    fun getUserId(): Long {
        val configuration = getCached()
        return if (configuration?.userId == null) 0L else configuration.userId
    }

    fun getLatitude(): Double? {
        val configuration = getCached()
        return configuration?.latitude
    }

    fun getLongitude(): Double? {
        val configuration = getCached()
        return configuration?.longitude
    }

    fun getEventId(): Long {
        var configuration = getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = 0
            )
            configuration.eventId = 0
        }
        configuration.eventId++
        save(configuration)

        return configuration.eventId
    }

}