package com.edtslib.di

import com.edtslib.data.source.remote.network.SentinelApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val mainAppModule = module {
    single { provideSentinelApiService(get(named("sentinel"))) }
}

private fun provideSentinelApiService(retrofit: Retrofit) =
    retrofit.create(SentinelApiService::class.java)