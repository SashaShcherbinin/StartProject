package feature.dashboard.di

import feature.dashboard.presentation.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

fun featureDashboardModule(): Module = module {
    viewModelOf(::DashboardViewModel)
}