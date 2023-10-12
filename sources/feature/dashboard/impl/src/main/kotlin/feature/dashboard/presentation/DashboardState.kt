package feature.dashboard.presentation

import base.arch.viewmodel.ContentState
import feature.user.domain.entity.User

data class DashboardState(
    val users: List<User> = emptyList(),
    val contentState: ContentState = ContentState.Loading,
    val errorResId: Int = 0,
)