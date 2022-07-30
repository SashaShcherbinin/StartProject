package com.start.domain.usecase

import com.start.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    fun getUser(): Flow<User>
}