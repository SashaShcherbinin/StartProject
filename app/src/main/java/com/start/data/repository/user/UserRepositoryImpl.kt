package com.start.data.repository.user

import com.start.data.common.cashe.CachePolicy
import com.start.data.common.storage.LocalStorage
import com.start.domain.entity.User
import com.start.domain.repository.UserRepository
import e.palyvo.domain.exeption.ServerException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

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