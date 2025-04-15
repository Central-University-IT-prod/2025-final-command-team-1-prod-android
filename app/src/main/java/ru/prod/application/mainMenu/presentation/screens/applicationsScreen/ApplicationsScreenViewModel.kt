package ru.prod.application.mainMenu.presentation.screens.applicationsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.put
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.data.ApiService
import ru.prod.application.data.BookRepository
import ru.prod.application.utils.Book
import javax.inject.Inject

@HiltViewModel
class ApplicationsScreenViewModel @Inject constructor(
    private val repository: BookRepository,
    private val apiService: ApiService,
    private val authManager: AuthManager,
    private val httpClient: HttpClient
) : ViewModel() {
    var books by mutableStateOf<List<Book>?>(null)
        private set
    var email by mutableStateOf("")
    var bookingId by mutableIntStateOf(0)

    var showGetConfirmationAlert by mutableStateOf(false)

    var showDeleteConfirmationAlert by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    fun load() {
        viewModelScope.launch {
            while (true) {
                repository.getBookedBooks().onSuccess {
                    books = it
                    error = null
                }.onFailure {
                    error = it.localizedMessage
                    books = emptyList()
                }
                delay(1000L)
            }
        }
    }

    fun confirmGetBook(rating: Int, comment: String, email: String) {
        viewModelScope.launch {
            try {
                books = books?.filter { book -> book.id != bookingId }

                apiService.addReview(rating, comment, email)

                authManager.token.value?.let { tokenSafe ->
                    httpClient.put("$BASE_URL/posts/$bookingId/mark-taken") {
                        bearerAuth(tokenSafe)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun confirmDeleteBook(rating: Int, comment: String, email: String) {
        viewModelScope.launch {
            try {
                books = books?.filter { book -> book.id != bookingId }

                apiService.addReview(rating, comment, email)

                authManager.token.value?.let { tokenSafe ->
                    httpClient.delete("$BASE_URL/posts/$bookingId/booking") {
                        bearerAuth(tokenSafe)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}