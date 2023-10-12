package feature.user.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import base.android.extention.navigate
import base.android.view.UploadingDialog
import coil.compose.rememberAsyncImagePainter
import feature.user.R
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterial3Api
@Composable
fun UserScreen(
    navController: NavController,
    userId: String,
    viewModel: UserViewModel = getViewModel(),
) {
    val state: UserState by viewModel.state.collectAsState()
    state.navigate.consume {
        navController.navigate(it)
    }
    if (state.itUploading) {
        UploadingDialog()
    }
    viewModel.setUserId(userId)
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.dashboard_user_dashboard))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.user_back_icon),
                            contentDescription = stringResource(id = R.string.user_close_button)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(state.user.photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.fieldName,
                    onValueChange = { viewModel.changeName(it) },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.fieldSurname,
                    onValueChange = { viewModel.changeSurname(it) },
                    label = { Text("Surname") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.fieldEmail,
                    onValueChange = { viewModel.changeEmail(it) },
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.save() }) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.delete() }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
