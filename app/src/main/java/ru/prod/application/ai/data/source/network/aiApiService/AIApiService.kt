package ru.prod.application.ai.data.source.network.aiApiService

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import io.ktor.utils.io.CancellationException
import ru.prod.application.auth.domain.FailedToFetchDataException
import ru.prod.application.core.domain.BASE_URL
import javax.inject.Inject

/* ApiService для работы с ИИ */
class AIApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    companion object {
        const val CHAT_ENDPOINT = "$BASE_URL/chat"
    }

    suspend fun getFullChat(token: String): Result<GetFullChatResponseModel> {
        return try {
            val response = httpClient.get(CHAT_ENDPOINT) {
                bearerAuth(token)
                parameter("limit", 100)
            }
            Result.success(response.body() as GetFullChatResponseModel)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(FailedToFetchDataException())
        }
    }

    suspend fun sendMessage(token: String, request: PostMessageRequestModel): Result<Unit> {
        return try {
            val response = httpClient.post(CHAT_ENDPOINT) {
                bearerAuth(token)
                setBody(request)
            }
            if (response.status.isSuccess()) {
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