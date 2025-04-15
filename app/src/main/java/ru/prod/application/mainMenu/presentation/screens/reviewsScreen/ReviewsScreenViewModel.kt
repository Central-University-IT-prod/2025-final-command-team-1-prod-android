package ru.prod.application.mainMenu.presentation.screens.reviewsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.mainMenu.data.repository.profileRepository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ReviewsScreenViewModel @Inject constructor(
    private val httpClient: HttpClient,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager
) : ViewModel() {
    private val _reviews = MutableStateFlow<List<Review>?>(null)
    val reviews: StateFlow<List<Review>?> = _reviews

    var error by mutableStateOf<String?>(null)
        private set

    fun loadData(username: String) {
        viewModelScope.launch {
            try {
                authManager.token.value?.let { tokenSafe ->
                    val response = httpClient.get("$BASE_URL/users/${username}/reviews") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(tokenSafe)
                    }
                    _reviews.value = response.body() as List<Review>
                }
            } catch (e: Exception) {
            }
        }
    }
}

@Serializable
data class Review(
    val comment: String,
    val reviewer_username: String,
    val created_at: String,
    val rating: Int
)