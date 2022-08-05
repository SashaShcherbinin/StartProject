package com.start.presentation.feature.main

import androidx.lifecycle.viewModelScope
import com.start.domain.entity.User
import com.start.domain.usecase.UserUseCase
import com.start.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import e.palyvo.domain.exeption.ServerException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(userUseCase: UserUseCase) : BaseViewModel() {

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
