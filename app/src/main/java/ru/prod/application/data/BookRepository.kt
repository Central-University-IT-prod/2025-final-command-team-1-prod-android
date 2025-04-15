package ru.prod.application.data

import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.utils.io.CancellationException
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.utils.Book
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager,
    private val httpClient: HttpClient,
) {

    suspend fun getAllBooks(): Result<List<Book>> {
        return try {
            val books = apiService.getAllBooks().map {
                Book(
                    id = it.id,
                    imageUrls = it.images,
                    name = it.title,
                    description = it.description,
                    summary = it.summary,
                    author = it.author,
                    condition = it.condition,
                    isFavorite = it.is_favorite,
                    genre = it.genre,
                    year = it.publication_year,
                    pages = it.pages_count,
                    userEmail = it.user_email,
                    cover = it.cover,
                    quote = it.quote,
                    username = it.owner_username ?: "",
                    locationAddress = it.place_address ?: "",
                    locationName = it.place_name ?: "",
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    suspend fun getFavoriteBooks(): Result<List<Book>> {
        return try {
            val books = apiService.getFavoriteBooks().map {
                Book(
                    id = it.id,
                    imageUrls = it.images,
                    name = it.title,
                    description = it.description,
                    summary = it.summary,
                    author = it.author,
                    condition = it.condition,
                    isFavorite = it.is_favorite,
                    genre = it.genre,
                    year = it.publication_year,
                    pages = it.pages_count,
                    userEmail = it.user_email,
                    cover = it.cover,
                    quote = it.quote,
                    username = it.owner_username ?: "",
                    locationAddress = it.place_address ?: "",
                    locationName = it.place_name ?: "",
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    suspend fun getBookedBooks(): Result<List<Book>> {
        return try {
            val books = apiService.getBookedBooks().map {
                Book(
                    id = it.id,
                    imageUrls = it.images,
                    name = it.title,
                    description = it.description,
                    summary = it.summary,
                    author = it.author,
                    condition = it.condition,
                    isFavorite = it.is_favorite,
                    genre = it.genre,
                    year = it.publication_year,
                    userEmail = it.user_email,
                    pages = it.pages_count,
                    cover = it.cover,
                    quote = it.quote,
                    username = it.owner_username ?: "",
                    locationAddress = it.place_address ?: "",
                    locationName = it.place_name ?: "",
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }


    suspend fun getBookById(id: Int): Result<Book> {
        return try {
            val book = apiService.getBookById(id)
            Result.success(book.toBook())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            val books = apiService.searchBooks(query).map {
                it.toBook()
            }
            Result.success(books)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
//
//    suspend fun addBook(book: Book): Result<Book> {
//        return try {
//            val addedBook = apiService.addBook(book).toBook()
//            Result.success(addedBook)
//        } catch (e: Exception) {
//            if (e is CancellationException) throw e
//            Result.failure(e)
//        }
//    }
//
//    suspend fun updateBook(id: String, book: Book): Result<Book> {
//        return try {
//            val updatedBook = apiService.updateBook(id, book).toBook()
//            Result.success(updatedBook)
//        } catch (e: Exception) {
//            if (e is CancellationException) throw e
//            Result.failure(e)
//        }
//    }
//
//    suspend fun deleteBook(id: String): Result<Unit> {
//        return try {
//            apiService.deleteBook(id)
//            Result.success(Unit)
//        } catch (e: Exception) {
//            if (e is CancellationException) throw e
//            Result.failure(e)
//        }
//    }

    suspend fun createBooking(id: Int) {
        val token = authManager.token.value ?: return
        try {
            httpClient.post(urlString = "$BASE_URL/posts/$id/booking"){
                bearerAuth(token)
            }
        } catch (e: Exception) {
        }
    }
}