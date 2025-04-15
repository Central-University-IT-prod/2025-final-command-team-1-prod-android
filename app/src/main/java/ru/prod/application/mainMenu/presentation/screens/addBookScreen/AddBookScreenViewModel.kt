package ru.prod.application.mainMenu.presentation.screens.addBookScreen

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.BASE_URL
import ru.prod.application.data.BookDto
import ru.prod.application.mainMenu.data.sorce.network.availibleLocations.AvailableLocationsApiService
import ru.prod.application.utils.book.LocationModel
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddBookScreenViewModel @Inject constructor(
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
    private val availableLocationsApiService: AvailableLocationsApiService
) : ViewModel() {
    var isLoading by mutableStateOf(false)

    private val _bookName = MutableStateFlow("")
    val bookName: StateFlow<String> = _bookName

    private val _bookAuthor = MutableStateFlow("")
    val bookAuthor: StateFlow<String> = _bookAuthor

    var year by mutableStateOf("")
        private set

    var pages by mutableStateOf("")
        private set

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedLocationId = MutableStateFlow(-1)
    val selectedLocationId: StateFlow<Int> = _selectedLocationId

    private val _dropdownExpanded = MutableStateFlow(false)
    val dropdownExpanded: StateFlow<Boolean> = _dropdownExpanded

    private val _availableLocations = MutableStateFlow<List<LocationModel>?>(null)
    val availableLocations: StateFlow<List<LocationModel>?> = _availableLocations

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images

    private val _addButtonAvailable = MutableStateFlow(false)
    val addButtonAvailable: StateFlow<Boolean> = _addButtonAvailable

    val conditions = listOf("Новое", "Хорошее", "Плохое")

    private val _selectedCondition = MutableStateFlow("")
    val selectedCondition: StateFlow<String> = _selectedCondition

    val genres = listOf("Детектив", "Драма", "Фантастика", "Научная")

    var selectedGenre by mutableStateOf<String>("")
        private set

    val covers = listOf("Твердый", "Мягкий")

    var selectedCover by mutableStateOf("")
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _availableLocations.value = availableLocationsApiService.getAvailableLocations()
        }
    }

    fun setBookName(value: String) {
        _bookName.value = value
        validateData()
    }

    fun setBookAuthor(value: String) {
        _bookAuthor.value = value
        validateData()
    }

    @JvmName("yearSet")
    fun setYear(value: String) {
        if (value.toIntOrNull() != null || value.isBlank()) {
            year = value
        }
        validateData()
    }


    @JvmName("pagesSet")
    fun setPages(value: String) {
        if (value.toIntOrNull() != null || value.isBlank()) {
            pages = value
        }
        validateData()
    }

    fun setDescription(value: String) {
        _description.value = value
        validateData()
    }

    fun setSelectedLocationId(id: Int) {
        _selectedLocationId.value = id
        _dropdownExpanded.value = false
        validateData()
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _dropdownExpanded.value = expanded
    }

    fun setSelectedCondition(value: String) {
        _selectedCondition.value = value
        validateData()
    }

    @JvmName("selectedCoverSet")
    fun setSelectedCover(value: String) {
        selectedCover = value
        validateData()
    }

    @JvmName("genreSelected")
    fun setSelectedGenre(value: String) {
        selectedGenre = value
        validateData()
    }

    fun addImage(image: String) {
        val newImagesList = images.value.toMutableList()
        newImagesList.add(image)
        _images.value = newImagesList
        validateData()
    }

    fun removeImageByIndex(index: Int) {
        val newImagesList = images.value.toMutableList()
        newImagesList.removeAt(index)
        _images.value = newImagesList
        validateData()
    }

    private fun validateData() {
        _addButtonAvailable.value =
            selectedLocationId.value >= 0 &&
                    bookName.value.isNotBlank() &&
                    bookAuthor.value.isNotBlank() &&
                    description.value.isNotBlank() &&
                    selectedCondition.value.isNotBlank() &&
                    selectedGenre.isNotBlank() &&
                    (year.toIntOrNull() != null || year.isBlank()) &&
                    images.value.isNotEmpty()
    }

    fun addBook(navigateToMainMenu: () -> Unit) {
        viewModelScope.launch(NonCancellable) {
            viewModelScope.launch {
                isLoading = true
                try {
                    authManager.token.value?.let { tokenSafe ->
                        val response = httpClient.post("$BASE_URL/posts") {
                            contentType(ContentType.Application.Json)
                            bearerAuth(tokenSafe)
                            setBody(
                                PostBookRequestModel(
                                    title = bookName.value,
                                    author = bookAuthor.value,
                                    description = description.value,
                                    condition = selectedCondition.value,
                                    images = listOf(),
                                    placeId = selectedLocationId.value,
                                    publicationYear = year.toIntOrNull() ?: 0,
                                    genre = selectedGenre,
                                    publisher = "publisher",
                                    pages_count = pages.toIntOrNull() ?: 0,
                                    cover = selectedCover,
                                )
                            )
                        }

                        val id = (response.body() as BookDto).id

                        println(response)
                        images.value.forEach {
                            val imageFile = File(it)

                            if (imageFile.exists()) {
                                val pngBytes = convertImageToPng(it)


                                pngBytes?.let { byteArray ->
                                    val res2 = httpClient.post("$BASE_URL/posts/$id/image") {
                                        contentType(ContentType.Application.Json)
                                        bearerAuth(tokenSafe)
                                        setBody(MultiPartFormDataContent(
                                            formData {
                                                append("image", byteArray, Headers.build {
                                                    append(
                                                        HttpHeaders.ContentType,
                                                        ContentType.Image.JPEG.toString() // Указать тип PNG
                                                    )
                                                    append(
                                                        HttpHeaders.ContentDisposition,
                                                        "filename=\"${imageFile.nameWithoutExtension}.jpeg\"" // Указать имя файла с расширением PNG
                                                    )
                                                })
                                            }
                                        ))
                                    }
                                    println(res2)
                                }

                            }
                        }
                    }
                    delay(500)
                    navigateToMainMenu()
                    delay(100)
                    isLoading = false
                } catch (e: Exception) {
                    navigateToMainMenu()
                }
            }
        }
    }
}

@Serializable
private data class PostBookRequestModel(
    val title: String,
    val author: String,
    val description: String,
    val condition: String,
    val images: List<String>,
    @SerialName("place_id") val placeId: Int,
    val genre: String,
    @SerialName("publication_year") val publicationYear: Int?,
    val publisher: String,
    val pages_count: Int?,
    val cover: String,
)

fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            it.getString(columnIndex)
        } else null
    }
}

fun convertImageToPng(imagePath: String): ByteArray? {
    val originalBitmap: Bitmap = BitmapFactory.decodeFile(imagePath)
    val outputStream = ByteArrayOutputStream()
    originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}
