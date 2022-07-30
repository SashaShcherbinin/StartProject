package com.start.data.repository.user

import com.start.domain.entity.User
import com.start.domain.repository.UserRepository
import e.palyvo.domain.exeption.ServerException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class UserRepositoryImpl
@Inject
constructor() : UserRepository {

    override fun getUser(): Flow<User> {
        return flow {
            while (true) {
                delay(1_000)
                emit(User("12", "User Name " + Random.nextInt()))
                if (Random.nextInt(6) == 2) throw ServerException("wow")
            }
        }
    }
}