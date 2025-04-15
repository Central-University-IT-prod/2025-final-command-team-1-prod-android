package ru.prod.application.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.prod.application.auth.data.source.local.authSource.AuthTokenSource
import javax.inject.Inject

/* Менеджер авторизации который хранит в себе ее состояние */
class AuthManager @Inject constructor(
    private val authTokenSource: AuthTokenSource
) {
    val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    init {
        _token.value = authTokenSource.getToken()
    }

    fun updateToken(value: String) {
        _token.value = value
        authTokenSource.saveToken(value)
    }

    fun resetToken() {
        _token.value = null
        authTokenSource.clearToken()
    }
}