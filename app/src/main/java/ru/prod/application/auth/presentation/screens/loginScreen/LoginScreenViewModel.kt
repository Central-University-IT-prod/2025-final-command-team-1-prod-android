package ru.prod.application.auth.presentation.screens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.prod.application.auth.data.repositories.authRepository.AuthRepository
import ru.prod.application.utils.general.LoadingState
import javax.inject.Inject

/* ViewModel экрана регистрации */
@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loadingState = MutableStateFlow(LoadingState.LOADED)
    val loadingState: StateFlow<LoadingState> = _loadingState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _submitEnabled = MutableStateFlow(false)
    val submitEnabled: StateFlow<Boolean> = _submitEnabled

    fun setEmail(value: String) {
        _loadingState.value = LoadingState.LOADED
        _email.value = value
        validateFields()
    }

    fun setPassword(value: String) {
        _loadingState.value = LoadingState.LOADED
        _password.value = value
        validateFields()
    }

    private fun validateFields() {
        _submitEnabled.value = _email.value.length >= 6 &&
                _password.value.length >= 6
    }

    fun submit(navigateToMainFlow: () -> Unit) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val signupResult = authRepository.login(email.value, password.value)
            if (signupResult.isFailure) {
                _loadingState.value = LoadingState.ERROR
            } else {
                navigateToMainFlow()
                _loadingState.value = LoadingState.LOADED
            }
        }
    }
}