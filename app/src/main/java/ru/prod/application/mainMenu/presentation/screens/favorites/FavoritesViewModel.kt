package ru.prod.application.mainMenu.presentation.screens.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.prod.application.data.BookRepository
import ru.prod.application.utils.Book
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    var books by mutableStateOf<List<Book>?>(null)
        private set

    var error by mutableStateOf<String?>(null)

    fun load() {
        viewModelScope.launch {
            repository.getFavoriteBooks().onSuccess {
                books = it
                error = null
            }.onFailure {
                error = it.localizedMessage
                books = emptyList()
            }
        }
    }

}
