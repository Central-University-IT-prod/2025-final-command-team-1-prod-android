package ru.prod.application.data

import android.content.SharedPreferences
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.utils.Book
import javax.inject.Inject
import javax.inject.Named

class ApiService @Inject constructor(
    private val client: HttpClient,
    private val authManager: AuthManager,
    @Named("shared_prefs") private val sharedPreferences: SharedPreferences
) {

    suspend fun getAllBooks(): List<BookDto> {
        return authManager.token.value?.let { tokenSafe ->
            val response = client.get("$BASE_URL/posts/available") {
                bearerAuth(tokenSafe)
                parameter("offset", 0)
                parameter("limit", Int.MAX_VALUE)
            }
            val s = response.body<String>()
            response.body()
        } ?: emptyList()
    }

    suspend fun getFavoriteBooks(): List<BookDto> {
        return authManager.token.value?.let { tokenSafe ->
            val response = client.get("$BASE_URL/posts/favorites") {
                bearerAuth(tokenSafe)
                parameter("offset", 0)
                parameter("limit", Int.MAX_VALUE)
            }
            response.body()
        } ?: emptyList()
    }

    suspend fun getBookedBooks(): List<BookDto> {
        return authManager.token.value?.let { tokenSafe ->
            val response = client.get("$BASE_URL/posts/booked") {
                bearerAuth(tokenSafe)
            }
            response.body()
        } ?: emptyList()
    }

    suspend fun getBookById(id: Int): BookDto {
        val token = authManager.token.value ?: throw Exception("Not authorized")
        return client.get("$BASE_URL/posts/$id") {
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            bearerAuth(token)
        }.body()
    }

    suspend fun searchBooks(query: String): List<BookDto> {
        return authManager.token.value?.let { tokenSafe ->
            client.get("$BASE_URL/posts/search") {
                parameter("query", query)
                bearerAuth(tokenSafe)
                parameter("offset", 0)
                parameter("limit", Int.MAX_VALUE)
            }.body()
        } ?: emptyList()
    }

    suspend fun addBookToFavorites(id: Int) {
        authManager.token.value?.let { tokenSafe ->
            val response = client.put("$BASE_URL/posts/$id/favorites") {
                bearerAuth(tokenSafe)
            }
        }
    }

    suspend fun removeBookFromFavorites(id: Int) {
        authManager.token.value?.let { tokenSafe ->
            val response = client.delete("$BASE_URL/posts/$id/favorites") {
                bearerAuth(tokenSafe)
            }
        }
    }

    suspend fun addBook(book: Book): BookDto {
        val token = authManager.token.value ?: throw Exception("Not authorized")
        return client.post("$BASE_URL/books") {
            contentType(ContentType.Application.Json)
            setBody(book)
            bearerAuth(token)
        }.body()
    }

    suspend fun updateBook(id: String, book: Book): BookDto {
        val token = authManager.token.value ?: throw Exception("Not authorized")
        return client.put("$BASE_URL/books/$id") {
            contentType(ContentType.Application.Json)
            setBody(book)
            bearerAuth(token)
        }.body()
    }

    suspend fun deleteBook(id: String) {
        val token = authManager.token.value ?: throw Exception("Not authorized")
        client.delete("$BASE_URL/books/$id") {
            bearerAuth(token)
        }
    }

    suspend fun addReview(rating: Int, comment: String, targetEmail: String) {
        authManager.token.value?.let { tokenSafe ->
            val res = client.post("$BASE_URL/reviews") {
                bearerAuth(tokenSafe)
                setBody(
                    ReviewDTO(
                        rating,
                        comment,
                        targetEmail
                    )
                )
            }
        }
    }

    suspend fun bindToken(token: String) {
        sharedPreferences.edit().putString("notifications_token", token).apply()

        Log.d("BIND TOKEN", token)
        if (authManager.token.value != null) {
            client.post("$BASE_URL/users/bind_token") {
                bearerAuth(authManager.token.value!!)
                setBody(BindTokenDto(token))
            }
        }
    }

    suspend fun bindToken() {
        val token: String? = sharedPreferences.getString("notifications_token", "")

        if (token != null && authManager.token.value != null) {
            client.post("$BASE_URL/users/bind_token") {
                bearerAuth(authManager.token.value!!)
                setBody(BindTokenDto(token))
            }
        }
    }

    suspend fun getQuoteOfTheDay(): Quote {
        val response = client.post(
            "http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=ru"
        ) {
            contentType(ContentType.Application.Json)
        }
        return response.body<Quote>()
    }


}

@Serializable
data class ReviewDTO(
    val rating: Int,
    val comment: String,
    val target_user_email: String
)

@Serializable
data class BindTokenDto(
    val token: String
)

@Serializable
data class Quote(
    val quoteText: String,
)
