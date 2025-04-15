package ru.prod.application.ai.data.source.network.aiApiService

import kotlinx.serialization.Serializable

@Serializable
data class PostMessageRequestModel(
    val text: String
)