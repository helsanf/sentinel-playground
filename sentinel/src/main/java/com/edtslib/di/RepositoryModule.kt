package com.edtslib.di

import com.edtslib.data.source.SentinelRepository
import com.edtslib.data.source.local.ConfigurationLocalSource
import com.edtslib.data.source.local.SentinelQueueLocalDataSource
import com.edtslib.data.source.remote.SentinelRemoteDataSource
import com.edtslib.domain.repository.ISentinelRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single { SentinelRemoteDataSource(get()) }
    single { SentinelQueueLocalDataSource(get(named("sentinelSharePref"))) }
    single { ConfigurationLocalSource(get(named("sentinelSharePref"))) }
    single<ISentinelRepository> {
        SentinelRepository(
            get(),
            get(),
            get(),
            androidContext()
        )
    }
}