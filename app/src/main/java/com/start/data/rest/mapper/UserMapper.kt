package com.start.data.rest.mapper

import com.start.data.rest.dto.UserDto
import com.start.domain.entity.User
import javax.inject.Inject

class UserMapper
@Inject
constructor() {

    fun map(userDto: UserDto): User = User(
        id = userDto.id,
        name = userDto.name
    )
}