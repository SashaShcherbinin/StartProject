package com.start.feature.main.data.repository

import com.start.base.data.common.cashe.CachePolicy
import com.start.base.data.common.storage.LocalStorage
import com.start.feature.main.data.storage.UserNetworkStorage
import com.start.feature.main.domain.entity.User
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
@Inject
constructor(private val networkStorage: UserNetworkStorage) : UserRepository {

    private val localStorage = LocalStorage<Unit, User>(
        maxElements = 1,
        cachePolicy = CachePolicy.create(10, TimeUnit.SECONDS), network = {
            networkStorage.getUser()
        })

    override fun getUser(): Flow<User> = localStorage.get(Unit)
}