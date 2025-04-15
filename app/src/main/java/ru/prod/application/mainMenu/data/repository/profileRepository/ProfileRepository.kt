package ru.prod.application.mainMenu.data.repository.profileRepository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.mainMenu.data.sorce.network.profileApiService.ProfileApiService
import ru.prod.application.mainMenu.presentation.screens.profileScreen.GetBookResponseModel
import ru.prod.application.mainMenu.presentation.screens.profileScreen.GetUserInfoResponseModel
import ru.prod.application.utils.Book
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApiService: ProfileApiService,
    private val authManager: AuthManager,
    private val httpClient: HttpClient,
) {
    suspend fun getProfile(): List<Book> {
        return profileApiService.getProfile()
    }

    suspend fun getMyBooks(): Triple<String, Float, List<Book>> = withContext(Dispatchers.IO) {
        try {
            authManager.token.value?.let { tokenSafe ->

                // Запускаем асинхронные запросы
                val userInfoDeferred = async {
                    httpClient.get("$BASE_URL/users/me") {
                        bearerAuth(tokenSafe)
                    }
                }

                val myBooksDeferred = async {
                    httpClient.get("$BASE_URL/posts/my") {
                        bearerAuth(tokenSafe)
                    }
                }

                // Ждем, пока оба запроса завершатся
                val getUserInfoResponse = userInfoDeferred.await()
                val getMyBooksResponse = myBooksDeferred.await()

                val username = (getUserInfoResponse.body() as GetUserInfoResponseModel).username
                val rating = (getUserInfoResponse.body() as GetUserInfoResponseModel).rating
                val books = (getMyBooksResponse.body() as List<GetBookResponseModel>).map {
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
                        year = it.publicationYear,
                        pages = it.pages_count,
                        userEmail = it.user_email,
                        cover = it.cover,
                        quote = it.quote,
                        username = "",
                        locationAddress = "",
                        locationName = ""
                    )
                }
                return@withContext Triple(username, rating, books)
            } ?: return@withContext Triple("", 0f, emptyList())
        } catch (e: Exception) {
            return@withContext Triple("", 0f, emptyList())
        }
    }

}
