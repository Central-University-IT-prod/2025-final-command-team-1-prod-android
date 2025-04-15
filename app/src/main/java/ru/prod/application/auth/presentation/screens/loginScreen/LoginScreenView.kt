package ru.prod.application.auth.presentation.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.prod.application.R
import ru.prod.application.core.presentation.customComponents.buttons.CustomButton
import ru.prod.application.core.presentation.customComponents.inputs.CustomTextField
import ru.prod.application.utils.general.LoadingState

/* View экрана регистрации */
@Composable
fun LoginScreenView(
    navigateToSignUpScreen: () -> Unit,
    navigateToMainFlow: () -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val loadingState = viewModel.loadingState.collectAsStateWithLifecycle()
    val submitEnabled = viewModel.submitEnabled.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(16.dp)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.displayLarge,
            color = colorResource(id = R.color.text)
        )

        Column {
            CustomTextField(
                value = email.value,
                onValueChange = viewModel::setEmail,
                placeholder = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth(),
                isError = loadingState.value == LoadingState.ERROR,
                readOnly = loadingState.value == LoadingState.LOADING
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = password.value,
                onValueChange = viewModel::setPassword,
                placeholder = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth(),
                isError = loadingState.value == LoadingState.ERROR,
                isSecured = true,
                readOnly = loadingState.value == LoadingState.LOADING
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.sign_up_hint),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.secondary_text)
            )

            if (loadingState.value == LoadingState.ERROR) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.signup_error),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.error)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CustomButton(
                text = stringResource(id = R.string.login_into_account),
                onCLick = {
                    focusManager.clearFocus()
                    viewModel.submit(navigateToMainFlow)
                },
                modifier = Modifier.fillMaxWidth(),
                isEnabled = submitEnabled.value,
                isLoading = loadingState.value == LoadingState.LOADING
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.to_signup_screen_first_part),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.secondary_text)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stringResource(id = R.string.to_signup_screen_second_part),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.base),
                    modifier = Modifier.clickable(onClick = navigateToSignUpScreen)
                )
            }
        }
    }
}