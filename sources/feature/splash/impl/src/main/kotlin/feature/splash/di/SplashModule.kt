package feature.splash.di

import feature.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

fun featureSplashModule(): Module = module {
    viewModelOf(::SplashViewModel)
}