package com.start.domain.usecase.impl

import com.start.domain.entity.User
import com.start.domain.repository.UserRepository
import com.start.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCaseImpl
@Inject
constructor(private val userRepository: UserRepository) : UserUseCase {

    override fun getUser(): Flow<User> = userRepository.getUser()
}