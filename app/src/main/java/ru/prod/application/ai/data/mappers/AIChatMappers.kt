package ru.prod.application.ai.data.mappers

import ru.prod.application.ai.data.source.network.aiApiService.MessageResponseModel
import ru.prod.application.ai.data.source.network.aiApiService.PostMessageRequestModel
import ru.prod.application.utils.aiChat.MessageAuthor
import ru.prod.application.utils.aiChat.MessageModel

/* Мапперы моделей данный ии чата */

fun MessageResponseModel.mapToMessageModel(): MessageModel {
    return MessageModel(
        text = text,
        author = if (role != "user") MessageAuthor.AI else MessageAuthor.AI
    )
}
fun MessageModel.mapToPostMessageRequestModel(): PostMessageRequestModel {
    return PostMessageRequestModel(
        text = text
    )
}