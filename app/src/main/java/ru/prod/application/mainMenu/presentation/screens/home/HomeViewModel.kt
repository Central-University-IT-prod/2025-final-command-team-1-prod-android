package ru.prod.application.mainMenu.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.prod.application.data.ApiService
import ru.prod.application.data.BookRepository
import ru.prod.application.data.Quote
import ru.prod.application.utils.Book
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BookRepository,
    private val apiService: ApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    var quote by mutableStateOf<Quote?>(null)
        private set

    init {
        loadQuote()
    }

    fun loadBooks() {
        viewModelScope.launch {
            try {
                val result = repository.getAllBooks()
                result.fold(
                    onSuccess = { books ->
                        _uiState.value = BookListUiState.Success(books)
                    },
                    onFailure = { error ->
                        println(error)
                        _uiState.value = BookListUiState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                println(e)
                if (e is CancellationException) throw e
                _uiState.value = BookListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                quote = apiService.getQuoteOfTheDay()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

sealed class BookListUiState {
    object Loading : BookListUiState()
    data class Success(val books: List<Book>) : BookListUiState()
    data class Error(val message: String) : BookListUiState()
}
