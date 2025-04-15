package ru.prod.application.utils

data class Book(
    val id: Int,
    val imageUrls: List<String>,
    val name: String,
    val description: String,
    val summary: String,
    val author: String,
    val condition: String,
    val isFavorite: Boolean,
    val genre: String,
    val userEmail: String,
    val year: Int?,
    val pages: Int?,
    val cover: String,
    val quote: String,
    val locationAddress: String?,
    val locationName: String?,
    val username: String?,
)
