package com.start.feature.main.di

import com.start.feature.main.data.repository.UserRepositoryImpl
import com.start.feature.main.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun getUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository =
        userRepositoryImpl

}