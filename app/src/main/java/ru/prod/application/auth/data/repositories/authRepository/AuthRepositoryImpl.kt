package ru.prod.application.auth.data.repositories.authRepository

import ru.prod.application.auth.AuthManager
import ru.prod.application.auth.data.source.network.authApi.AuthApiService
import ru.prod.application.auth.data.source.network.authApi.LoginRequestModel
import ru.prod.application.auth.data.source.network.authApi.SignUpRequestModel
import ru.prod.application.data.ApiService
import javax.inject.Inject

/* Репозиторий для работы с авторизацией */
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val authManager: AuthManager,
    private val apiService: ApiService
): AuthRepository {
    override suspend fun getToken(): String? {
        return authManager.token.value
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = authApiService.login(LoginRequestModel(email, password))
            val token = response.getOrThrow().token
            authManager.updateToken(token)
            apiService.bindToken()
            Result.success(Unit)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, phoneNumber: String, password: String): Result<Unit> {
        val signUpResponse = authApiService.signUp(SignUpRequestModel(email, phoneNumber, password))
        if (signUpResponse.isSuccess) {
            return login(email, password)
        }
        return signUpResponse
    }
}