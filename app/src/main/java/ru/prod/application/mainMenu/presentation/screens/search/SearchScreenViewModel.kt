package ru.prod.application.mainMenu.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.prod.application.data.ApiService
import ru.prod.application.data.BookRepository
import ru.prod.application.mainMenu.presentation.screens.home.BookListUiState
import ru.prod.application.utils.Book
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val httpClient: HttpClient
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _books = MutableStateFlow<List<Book>?>(null)
    val books: StateFlow<List<Book>?> = _books

    private var searchJob: Job? = null

    fun setQuery(value: String) {
        _isLoading.value = true
        _query.value = value
        if (value.isNotBlank()) {
            search()
        }
    }

    fun search() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            ensureActive()
            try {
                val result = bookRepository.searchBooks(query.value)
                result.fold(
                    onSuccess = { books ->
                        _books.value = books
                    },
                    onFailure = { _ ->
                        _books.value = emptyList()
                    }
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
            _isLoading.value = false
        }
    }
}