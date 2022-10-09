package com.start.feature.main.domain.usecase

import com.start.feature.main.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface GetUserUseCase: () -> Flow<User>