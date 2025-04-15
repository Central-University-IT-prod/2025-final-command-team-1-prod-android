package ru.prod.application.mainMenu.presentation.screens.details

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.prod.application.data.ApiService
import ru.prod.application.data.BookRepository
import ru.prod.application.utils.Book
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
    private val apiService: ApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    private var bookId: Int? = null

    fun setBookId(id: Int) {
        if (bookId == null) {
            bookId = id
            loadBookDetails()
        }
    }

    private fun loadBookDetails() {
        val bookId = this.bookId ?: return
        viewModelScope.launch {
            _uiState.value = BookDetailUiState.Loading
            try {
                val result = repository.getBookById(bookId)
                result.fold(
                    onSuccess = { book ->
                        _uiState.value = BookDetailUiState.Success(book)
                    },
                    onFailure = { error ->
                        _uiState.value = BookDetailUiState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.value = BookDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createRequest(bookId: Int) {
        viewModelScope.launch {
            repository.createBooking(bookId)
        }
    }

    fun onFavorite(bookId: Int, newStatus: Boolean, callback: () -> Unit) {
        viewModelScope.launch {
            if (newStatus) {
                apiService.addBookToFavorites(bookId)
            } else {
                apiService.removeBookFromFavorites(bookId)
            }
            callback()
        }
    }
}

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val book: Book) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}
