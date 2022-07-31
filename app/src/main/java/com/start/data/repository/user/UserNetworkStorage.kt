package com.start.data.repository.user

import com.start.data.rest.mapper.UserMapper
import com.start.data.rest.service.ApiService
import com.start.domain.entity.User
import retrofit2.Retrofit
import javax.inject.Inject

class UserNetworkStorage
@Inject
constructor(retrofit: Retrofit, private val userMapper: UserMapper) {

    private val _service: ApiService = retrofit.create(ApiService::class.java)

    suspend fun getUser(): User = userMapper.map(_service.getUser())
}