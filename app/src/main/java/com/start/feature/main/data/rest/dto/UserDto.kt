package com.start.feature.main.data.rest.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
)