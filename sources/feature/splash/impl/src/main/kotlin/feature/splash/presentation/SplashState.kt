package feature.splash.presentation

import base.arch.viewmodel.navigation.Direction
import base.arch.viewmodel.event.ConsumedEvent
import base.arch.viewmodel.event.Event

data class SplashState(
    val navigate: Event<Direction> = ConsumedEvent
)