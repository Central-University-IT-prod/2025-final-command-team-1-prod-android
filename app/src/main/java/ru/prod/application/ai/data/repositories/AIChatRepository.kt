package ru.prod.application.ai.data.repositories

import ru.prod.application.utils.aiChat.MessageModel

interface AIChatRepository {
    suspend fun loadChat(): Result<List<MessageModel>>
    suspend fun sendMessage(messageModel: MessageModel): Result<List<MessageModel>>
}