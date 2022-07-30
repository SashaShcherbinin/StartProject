package com.start.domain.repository

import com.start.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(): Flow<User>
}