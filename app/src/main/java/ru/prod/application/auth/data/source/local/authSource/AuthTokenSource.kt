package ru.prod.application.auth.data.source.local.authSource

import android.content.SharedPreferences
import javax.inject.Inject

/* Класс для работы с encrypted хранилищем токена */
class AuthTokenSource @Inject constructor(
    private val encryptedSharedPreferences: SharedPreferences
) {
    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    fun saveToken(token: String) {
        encryptedSharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getToken(): String? {
        return encryptedSharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        encryptedSharedPreferences.edit()
            .remove(TOKEN_KEY)
            .apply()
    }
}