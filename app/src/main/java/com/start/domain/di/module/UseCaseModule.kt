package com.start.domain.di.module

import com.start.domain.usecase.UserUseCase
import com.start.domain.usecase.impl.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun getUserUseCase(userUseCase: UserUseCaseImpl): UserUseCase = userUseCase

}