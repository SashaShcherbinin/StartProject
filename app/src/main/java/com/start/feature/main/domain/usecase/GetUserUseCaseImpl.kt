package com.start.feature.main.domain.usecase

import com.start.feature.main.domain.usecase.GetUserUseCase
import com.start.feature.main.data.repository.UserRepository
import com.start.feature.main.domain.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCaseImpl
@Inject
constructor(private val userRepository: UserRepository) : GetUserUseCase {

    override fun invoke(): Flow<User> = userRepository.getUser()
}