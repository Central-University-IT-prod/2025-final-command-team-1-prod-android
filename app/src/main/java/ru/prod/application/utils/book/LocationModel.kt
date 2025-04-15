package ru.prod.application.utils.book

import kotlinx.serialization.Serializable

@Serializable
data class LocationModel(
    val id: Int,
    val name: String,
    val description: String,
    val city: String,
    val address: String
)
