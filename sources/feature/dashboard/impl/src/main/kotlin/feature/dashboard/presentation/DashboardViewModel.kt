package feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import base.arch.viewmodel.ContentState
import base.logger.AppLog
import feature.dashboard.R
import feature.user.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class DashboardViewModel(getUsersUseCase: GetUsersUseCase, private val appLog: AppLog) :
    ViewModel() {

    private val _state: MutableStateFlow<DashboardState> = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getUsersUseCase()
            .onStart { _state.update { it.copy(contentState = ContentState.Loading) } }
            .onEach { users ->
                _state.update {
                    it.copy(users = users, contentState = ContentState.Content)
                }
            }
            .catch { e ->
                appLog.e(e = e)
                _state.update {
                    it.copy(
                        contentState = ContentState.Error,
                        errorResId = R.string.dashboard_network_error,
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
