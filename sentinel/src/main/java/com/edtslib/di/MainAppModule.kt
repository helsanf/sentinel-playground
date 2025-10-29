package com.edtslib.di

import androidx.work.WorkManager
import com.edtslib.data.source.remote.network.SentinelApiService
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val mainAppModule = module {
    single { provideSentinelApiService(get(named("sentinel"))) }
    single { WorkManager.getInstance(androidContext()) }
}

private fun provideSentinelApiService(retrofit: Retrofit) =
    retrofit.create(SentinelApiService::class.java)