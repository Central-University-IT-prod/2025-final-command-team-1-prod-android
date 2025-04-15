package ru.prod.application.ai.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.prod.application.R
import ru.prod.application.ai.presentation.screens.views.AIErrorView
import ru.prod.application.ai.presentation.screens.views.ChatMessageTextField
import ru.prod.application.ai.presentation.screens.views.MessageView
import ru.prod.application.utils.general.LoadingState

/* View чата с ИИ */
@Composable
fun AIChatScreenView(viewModel: AIScreenViewModel = hiltViewModel()) {
    val loadingState = viewModel.loadingState.collectAsStateWithLifecycle()
    val message = viewModel.message.collectAsStateWithLifecycle()
    val chat = viewModel.chat.collectAsStateWithLifecycle()

    val chatScrollState = rememberLazyListState()

    LaunchedEffect(chat.value) { // При появлении нового сообщения скролим в самый низ
        if (chat.value.isNotEmpty()) {
            chatScrollState.animateScrollToItem(chat.value.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                state = chatScrollState
            ) {
                items(chat.value) {
                    MessageView(message = it)
                }
            }

            if (loadingState.value == LoadingState.ERROR) {
                AIErrorView(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                )
            }
        }

        ChatMessageTextField(
            value = message.value,
            onValueChange = viewModel::setMessage,
            send = viewModel::sendMessage,
            loadingState = loadingState.value,
            isEnabled = message.value.isNotBlank()
        )
    }
}