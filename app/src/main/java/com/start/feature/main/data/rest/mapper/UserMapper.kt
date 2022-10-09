package com.start.feature.main.data.rest.mapper

import com.start.feature.main.data.rest.dto.UserDto
import com.start.feature.main.domain.entity.User
import javax.inject.Inject

class UserMapper
@Inject
constructor() {

    fun map(userDto: UserDto): User = User(
        id = userDto.id,
        name = userDto.name
    )
}