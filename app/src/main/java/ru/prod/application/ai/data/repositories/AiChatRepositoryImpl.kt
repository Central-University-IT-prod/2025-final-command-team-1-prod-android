package ru.prod.application.ai.data.repositories

import io.ktor.utils.io.CancellationException
import ru.prod.application.ai.data.mappers.mapToMessageModel
import ru.prod.application.ai.data.mappers.mapToPostMessageRequestModel
import ru.prod.application.ai.data.source.network.aiApiService.AIApiService
import ru.prod.application.auth.AuthManager
import ru.prod.application.auth.domain.FailedToFetchDataException
import ru.prod.application.utils.aiChat.MessageModel
import javax.inject.Inject

/* Репозиторий ии чата */
class AiChatRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val aiApiService: AIApiService
) : AIChatRepository {
    private var messages = mutableListOf<MessageModel>()

    override suspend fun loadChat(): Result<List<MessageModel>> {
        return try {
            val response = aiApiService.getFullChat(authManager.token.value ?: "")
            messages = response.getOrThrow().messages.map { it.mapToMessageModel() }.toMutableList()
            Result.success(messages)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(FailedToFetchDataException())
        }
    }

    override suspend fun sendMessage(messageModel: MessageModel): Result<List<MessageModel>> {
        val postMessageResponse =
            aiApiService.sendMessage(authManager.token.value ?: "", messageModel.mapToPostMessageRequestModel())

        return if (postMessageResponse.isSuccess) {
            loadChat()
        } else {
            Result.failure(FailedToFetchDataException())
        }
    }
}