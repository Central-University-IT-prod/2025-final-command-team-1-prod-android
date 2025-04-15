package ru.prod.application.core.data.source.network.ktorSample

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import ru.prod.application.core.domain.BASE_URL
import javax.inject.Inject

// Пример реализации запросов в сеть
class KtorSample @Inject constructor(private val httpClient: HttpClient) {
    companion object {
        val ENTRIES = "$BASE_URL/uuid"
    }

    suspend fun getRequest(): HttpResponse {
        return httpClient.get(ENTRIES)
    }
}