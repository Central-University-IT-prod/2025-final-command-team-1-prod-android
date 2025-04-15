package ru.prod.application.auth.data.repositories.authRepository

interface AuthRepository {
    suspend fun getToken(): String?
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, phoneNumber: String, password: String): Result<Unit>
}