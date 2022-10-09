package com.start.feature.main.data.storage

import com.start.feature.main.data.rest.mapper.UserMapper
import com.start.feature.main.data.rest.service.ApiService
import com.start.feature.main.domain.entity.User
import retrofit2.Retrofit
import javax.inject.Inject

class UserNetworkStorage
@Inject
constructor(retrofit: Retrofit, private val userMapper: UserMapper) {

    private val _service: ApiService = retrofit.create(ApiService::class.java)

    suspend fun getUser(): User = userMapper.map(_service.getUser())
}