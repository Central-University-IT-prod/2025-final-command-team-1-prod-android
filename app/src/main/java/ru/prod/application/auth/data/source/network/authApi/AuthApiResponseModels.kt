package ru.prod.application.auth.data.source.network.authApi

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    val token: String
)