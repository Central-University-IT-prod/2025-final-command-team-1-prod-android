package ru.prod.application.data

import kotlinx.serialization.Serializable
import ru.prod.application.utils.Book

@Serializable
data class BookDto(
    val author: String,
    val condition: String,
    val created_at: String,
    val description: String,
    val genre: String,
    val id: Int,
    val images: List<String>,
    val is_favorite: Boolean,
    val place_id: Int,
    val publication_year: Int,
    val publisher: String,
    val status: String,
    val title: String,
    val user_email: String,
    val cover: String,
    val pages_count: Int,
    val summary: String,
    val quote: String,
    val place_address: String? = null,
    val place_name: String? = null,
    val owner_username: String? = null,
)

fun BookDto.toBook(): Book {
    return Book(
        id = id,
        imageUrls = images,
        name = title,
        description = description,
        author = author,
        condition = condition,
        isFavorite = is_favorite,
        genre = genre,
        year = publication_year,
        summary = summary,
        pages = pages_count,
        userEmail = user_email,
        cover = cover,
        quote = quote,
        username = owner_username ?: "",
        locationName = place_name ?: "",
        locationAddress = place_address ?: "",
    )
}
