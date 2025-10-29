package com.edtslib.data.base

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.util.*
import androidx.core.content.edit

abstract class LocalDataSource<T>(private val sharedPreferences: SharedPreferences) {
    abstract fun getKeyName(): String
    abstract fun getValue(json: String): T
    open fun expiredInterval() = 0 // in second

    private fun getKeyTimeName() = getKeyName().plus("_expired")

    open fun save(data: T?) {
        try {
            if (data == null) return

            sharedPreferences.edit {

                putString(getKeyName(), Gson().toJson(data))
                if (expiredInterval() > 0) {
                    putLong(getKeyTimeName(), Date().time)
                }
            }
        }
        catch (_: OutOfMemoryError) {

        }
        catch (_: Exception) {

        }
    }

    open fun get(): Flow<T> {
        val json = sharedPreferences.getString(getKeyName(), null)
        if (json.isNullOrEmpty()) return MutableLiveData<T>().apply { postValue(null) }.asFlow()

        if (expiredInterval() > 0) {
            val lastUpdate = sharedPreferences.getLong(getKeyTimeName(), 0)
            if (lastUpdate > 0) {
                val expiredTime = lastUpdate + expiredInterval() * 1000
                if (Date().time >= expiredTime) {
                    return MutableLiveData<T>().apply { postValue(null) }.asFlow()
                }
            }
        }

        return MutableLiveData<T>().apply { postValue(getValue(json)) }.asFlow()
    }

    open fun clear() {
        sharedPreferences.edit {
            remove(getKeyName())
        }
    }

    open fun getCached(): T? {
        try {
            val json = sharedPreferences.getString(getKeyName(), null)
            if (json.isNullOrEmpty()) return null

            if (expiredInterval() > 0) {
                val lastUpdate = sharedPreferences.getLong(getKeyTimeName(), 0)
                if (lastUpdate > 0) {
                    val expiredTime = lastUpdate + expiredInterval() * 1000
                    if (Date().time >= expiredTime) {
                        return null
                    }
                }
            }

            return getValue(json)
        }
        catch (_: Exception) {
            return null
        }
    }
}