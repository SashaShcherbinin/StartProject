package base.arch.viewmodel.navigation


sealed class Direction

data class Forward(
    val route: String,
    val popUpRoute: String? = null,
    val inclusive: Boolean = true,
) : Direction()

data object Back : Direction()