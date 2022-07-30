package com.start.presentation.feature.main

import androidx.lifecycle.viewModelScope
import com.start.domain.entity.User
import com.start.domain.usecase.UserUseCase
import com.start.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(userUseCase: UserUseCase) : BaseViewModel() {

    val user: StateFlow<User?> = userUseCase.getUser()
        .connectContentState()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

}