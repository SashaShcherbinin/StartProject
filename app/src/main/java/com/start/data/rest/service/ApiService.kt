package com.start.data.rest.service

import com.start.data.rest.dto.UserDto
import retrofit2.http.GET

interface ApiService {

    @GET("/v3/f5a898c4-ea10-4808-8207-46838aa13e8b")
    suspend fun getUser(): UserDto

}