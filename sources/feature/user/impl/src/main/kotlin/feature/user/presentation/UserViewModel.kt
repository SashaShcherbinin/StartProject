@file:Suppress("OPT_IN_USAGE")

package feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import base.arch.viewmodel.event.asOneTimeEvent
import base.arch.viewmodel.navigation.Back
import base.logger.AppLog
import feature.user.domain.usecase.DeleteUserUseCase
import feature.user.domain.usecase.GetUserUseCase
import feature.user.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    getUserUseCase: GetUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val appLog: AppLog
) : ViewModel() {

    private val _state: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        state.map { newState ->
            newState.userId
        }.filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { userId ->
                observeUserInfo(getUserUseCase, userId)
            }
            .catch { e ->
                appLog.e(e = e)
            }.take(1)
            .launchIn(viewModelScope)
    }

    private fun observeUserInfo(
        getUserUseCase: GetUserUseCase, userId: String
    ) = getUserUseCase(userId).onEach { user ->
        _state.update { currentState ->
            currentState.copy(
                user = user,
                fieldName = user.name,
                fieldSurname = user.surname,
                fieldEmail = user.email,
            )
        }
    }

    fun setUserId(userId: String) {
        _state.update {
            it.copy(userId = userId)
        }
    }

    fun changeName(name: String) {
        _state.update {
            it.copy(fieldName = name)
        }
    }

    fun changeSurname(surname: String) {
        _state.update {
            it.copy(fieldSurname = surname)
        }
    }

    fun changeEmail(email: String) {
        _state.update {
            it.copy(fieldEmail = email)
        }
    }

    fun save() {
        viewModelScope.launch {
            _state.update {
                it.copy(itUploading = true)
            }
            state.value.let { currentState ->
                val user = currentState.user
                user.copy(
                    name = currentState.fieldName,
                    surname = currentState.fieldSurname,
                    email = currentState.fieldEmail,
                )
            }.let { user ->
                updateUserUseCase(user)
                _state.update {
                    it.copy(itUploading = false)
                }
            }
            _state.update {
                it.copy(navigate = Back.asOneTimeEvent())
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            _state.update {
                it.copy(itUploading = true)
            }
            deleteUserUseCase(state.value.userId!!)
            _state.update {
                it.copy(itUploading = false)
            }
            _state.update {
                it.copy(navigate = Back.asOneTimeEvent())
            }
        }
    }
}
