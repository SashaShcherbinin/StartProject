package feature.splash.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import base.android.extention.navigate
import base.android.view.LoadingView
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = getViewModel()) {
    val state: SplashState by viewModel.state.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LoadingView()
    }
    state.navigate.consume {
        navController.popBackStack()
        navController.navigate(it)
    }
}
