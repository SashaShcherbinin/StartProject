package com.start.feature.main.data.repository

import com.start.feature.main.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(): Flow<User>
}