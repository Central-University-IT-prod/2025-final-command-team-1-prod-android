package ru.prod.application.auth.data.source.network.authApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import io.ktor.utils.io.CancellationException
import ru.prod.application.auth.domain.FailedToFetchDataException
import ru.prod.application.core.domain.BASE_URL
import javax.inject.Inject

/* ApiService для работы с эндпоинтами авторизации */
class AuthApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    companion object {
        const val LOGIN_ENDPOINT = "$BASE_URL/auth"
        const val SIGN_UP_ENDPOINT = "$BASE_URL/users"
    }

    suspend fun login(request: LoginRequestModel): Result<LoginResponseModel> {
        return try {
            val response = httpClient.post(LOGIN_ENDPOINT) {
                setBody(request)
            }
            return Result.success(response.body() as LoginResponseModel)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(FailedToFetchDataException())
        }
    }

    suspend fun signUp(request: SignUpRequestModel): Result<Unit> {
        return try {
            val response = httpClient.post(SIGN_UP_ENDPOINT) {
                setBody(request)
            }

            return if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(FailedToFetchDataException())
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(FailedToFetchDataException())
        }
    }
}