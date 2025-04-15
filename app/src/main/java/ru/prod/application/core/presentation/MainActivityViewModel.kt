package ru.prod.application.core.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.prod.application.auth.AuthManager
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    authManager: AuthManager
) : ViewModel() {

    var isAuthorized by mutableStateOf(authManager.token.value != null)

    init {
        viewModelScope.launch {
            authManager.token.collect {
                isAuthorized = it != null
            }
        }
    }

}