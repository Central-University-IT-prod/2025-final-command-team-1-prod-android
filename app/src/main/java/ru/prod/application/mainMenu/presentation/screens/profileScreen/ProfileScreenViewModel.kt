package ru.prod.application.mainMenu.presentation.screens.profileScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.mainMenu.data.repository.profileRepository.ProfileRepository
import ru.prod.application.utils.Book
import ru.prod.application.utils.general.LoadingState
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager
) : ViewModel() {
    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username

    private val _rating = MutableStateFlow(0f)
    val rating: StateFlow<Float> = _rating

    private val _books = MutableStateFlow<List<Book>?>(null)
    val books: StateFlow<List<Book>?> = _books

    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadData()
    }

    fun logout() {
        authManager.resetToken()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                val response = profileRepository.getMyBooks()
                _username.value = response.first
                _rating.value = response.second
                _books.value = response.third
                println(response)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                error = e.localizedMessage
            } finally {
                _loadingState.value = LoadingState.LOADED
            }
        }
    }
}

@Serializable
data class GetUserInfoResponseModel(
    val username: String,
    val rating: Float
)

@Serializable
data class GetBookResponseModel(
    val id: Int,
    val title: String,
    val author: String,
    val description: String,
    val condition: String,
    val images: List<String>,
    @SerialName("place_id") val placeId: Int,
    val genre: String,
    @SerialName("publication_year") val publicationYear: Int,
    val publisher: String,
    val user_email: String,
    val pages_count: Int,
    val cover: String,
    val is_favorite: Boolean,
    val summary: String,
    val quote: String
)