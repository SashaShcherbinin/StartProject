package feature.splash.presentation

import androidx.lifecycle.ViewModel
import base.arch.viewmodel.navigation.Forward
import base.arch.viewmodel.event.asOneTimeEvent
import feature.dashboard.navigation.SCREEN_DASHBOARD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel : ViewModel() {

    private val _state: MutableStateFlow<SplashState> = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(navigate = Forward(SCREEN_DASHBOARD).asOneTimeEvent()) }
    }

}
