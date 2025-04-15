package ru.prod.application.auth.data.source.network.authApi

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestModel(
    val email: String,
    val username: String,
    val password: String
)

@Serializable
data class LoginRequestModel(
    val email: String,
    val password: String
)