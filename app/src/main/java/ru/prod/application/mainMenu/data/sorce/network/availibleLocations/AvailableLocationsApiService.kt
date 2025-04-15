package ru.prod.application.mainMenu.data.sorce.network.availibleLocations

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.prod.application.auth.AuthManager
import ru.prod.application.auth.domain.FailedToFetchDataException
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.utils.book.LocationModel
import javax.inject.Inject

class AvailableLocationsApiService @Inject constructor(
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) {
    suspend fun getAvailableLocations(): List<LocationModel> {
        return authManager.token.value?.let { tokenSafe ->
            try {
                httpClient.get("$BASE_URL/places") {
                    bearerAuth(tokenSafe)
                }.bodyAsText()
            } catch (e: Exception) {
                return emptyList()
            }
        }?.let { responseText ->
            try {
                Json.decodeFromString<List<LocationModel>>(responseText)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
}