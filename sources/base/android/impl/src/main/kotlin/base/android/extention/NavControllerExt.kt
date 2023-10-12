package base.android.extention

import android.content.Context
import androidx.navigation.NavController
import base.arch.viewmodel.navigation.Back
import base.arch.viewmodel.navigation.Direction
import base.arch.viewmodel.navigation.Forward

fun NavController.navigate(direction: Direction) {
    when (direction) {
        is Forward -> navigate(direction.route) {
            direction.popUpRoute?.let { popBackStack(it, inclusive = direction.inclusive) }
        }
        Back -> popBackStack()
    }
}

fun NavController.goBack(context: Context) {
    if (popBackStack().not()) context.findActivity().finish()
}