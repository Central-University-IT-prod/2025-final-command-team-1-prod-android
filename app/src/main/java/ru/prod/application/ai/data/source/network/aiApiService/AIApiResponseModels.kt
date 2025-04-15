package ru.prod.application.ai.data.source.network.aiApiService

import kotlinx.serialization.Serializable

@Serializable
data class GetFullChatResponseModel(
    val messages: List<MessageResponseModel>
)

@Serializable
data class MessageResponseModel(
    val role: String,
    val text: String
)