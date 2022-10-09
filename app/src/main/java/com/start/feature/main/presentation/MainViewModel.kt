package com.start.feature.main.presentation

import androidx.lifecycle.viewModelScope
import com.start.feature.main.domain.entity.User
import com.start.feature.main.domain.usecase.GetUserUseCase
import com.start.base.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.start.base.domain.exeption.ServerException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(userUseCase: GetUserUseCase) : BaseViewModel() {

    val user: StateFlow<User?> = userUseCase.getUser()
        .connectContentState()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    fun openSomething() {
        viewModelScope.execute {
            delay(2_000)
            throw ServerException("test error")
        }
    }
}
