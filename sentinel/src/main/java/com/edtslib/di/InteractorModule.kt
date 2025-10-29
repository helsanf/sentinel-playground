package com.edtslib.di

import com.edtslib.domain.usecase.SentinelInteractor
import com.edtslib.domain.usecase.SentinelUseCase
import org.koin.dsl.module

val interactorModule = module {
    factory<SentinelUseCase> { SentinelInteractor(get()) }
}