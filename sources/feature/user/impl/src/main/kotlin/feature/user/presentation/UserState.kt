package feature.user.presentation

import base.arch.viewmodel.navigation.Direction
import base.arch.viewmodel.event.ConsumedEvent
import base.arch.viewmodel.event.Event
import feature.user.domain.entity.User

data class UserState(
    val userId: String? = null,
    val fieldName: String = "",
    val fieldSurname: String = "",
    val fieldEmail: String = "",
    val itUploading: Boolean = false,
    val user: User = User(id = "", name = "", surname = "", email = "", photoUrl = ""),
    val navigate: Event<Direction> = ConsumedEvent
)