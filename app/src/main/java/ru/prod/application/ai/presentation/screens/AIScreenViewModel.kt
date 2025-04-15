package ru.prod.application.ai.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.prod.application.ai.data.repositories.AIChatRepository
import ru.prod.application.utils.aiChat.MessageAuthor
import ru.prod.application.utils.aiChat.MessageModel
import ru.prod.application.utils.general.LoadingState
import javax.inject.Inject

/* ViewModel экрана чата ИИ */
@HiltViewModel
class AIScreenViewModel @Inject constructor(
    private val aiChatRepository: AIChatRepository
) : ViewModel() {
    private val _loadingState = MutableStateFlow(LoadingState.LOADED)
    val loadingState: StateFlow<LoadingState> = _loadingState

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _chat = MutableStateFlow<List<MessageModel>>(emptyList())
    val chat: StateFlow<List<MessageModel>> = _chat

    init {
        viewModelScope.launch {
            _chat.value = aiChatRepository.loadChat().getOrNull() ?: emptyList()
        }
    }

    fun setMessage(message: String) {
        _message.value = message
    }

    fun sendMessage() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val chatCopy = chat.value.toMutableList()
            chatCopy.add(MessageModel(message.value, MessageAuthor.USER))
            _chat.value = chatCopy
            val sendMessageResponse = aiChatRepository.sendMessage(MessageModel(message.value, MessageAuthor.USER))
            if (sendMessageResponse.isFailure) {
                _loadingState.value = LoadingState.ERROR
            } else {
                _loadingState.value = LoadingState.LOADED
            }
            _message.value = ""
        }
    }
}