package base.logger.di

import base.common.exctention.factoryCastOf
import base.logger.AppLog
import base.logger.AppLogImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun loggerModule(): Module = module {
    factoryCastOf(::AppLogImpl, AppLog::class)
}
