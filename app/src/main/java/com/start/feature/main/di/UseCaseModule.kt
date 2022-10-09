package com.start.feature.main.di

import com.start.feature.main.domain.usecase.GetUserUseCase
import com.start.feature.main.domain.usecase.GetUserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun getUserUseCase(userUseCase: GetUserUseCaseImpl): GetUserUseCase = userUseCase

}