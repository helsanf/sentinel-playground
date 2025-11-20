package com.edtslib.di

import com.edtslib.SentinelViewModel
import com.edtslib.domain.usecase.SentinelInteractor
import com.edtslib.domain.usecase.SentinelUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { SentinelViewModel(get()) }
    factory<SentinelUseCase> { SentinelInteractor(get()) }
}