package ru.prod.application.mainMenu.presentation.screens.details

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import okhttp3.internal.userAgent
import ru.prod.application.R
import ru.prod.application.core.presentation.customComponents.buttons.CustomButton
import ru.prod.application.core.presentation.customComponents.modals.ConfirmationAlert
import ru.prod.application.utils.Book

@Composable
fun BookDetailsScreen(
    bookId: Int,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navigateToBrowsePhoto: (imageUrl: String) -> Unit,
    navigateToApplications: () -> Unit,
    inSearch: Boolean = false,
    isMyBook: Boolean,
) {
    LaunchedEffect(bookId) {
        viewModel.setBookId(bookId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showRequestConfirmationDialog by remember { mutableStateOf(false) }

    if (showRequestConfirmationDialog) {
        ConfirmationAlert(
            "Создать заявку",
            text = "Вы уверены, что хотите создать заявку",
            confirmButtonText = "Да",
            denyButtonText = "Отменить",
            onConfirm = {
                viewModel.createRequest(bookId)
                showRequestConfirmationDialog = false
                navigateToApplications()
            },
            onDismissRequest = {
                showRequestConfirmationDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is BookDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = colorResource(id = R.color.darkest),
                        trackColor = Color.Transparent,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(28.dp)
                    )
                }
            }

            is BookDetailUiState.Success -> {
                val book = (uiState as BookDetailUiState.Success).book
                Column(Modifier.align(Alignment.Start)) {
                    BookDetailsContent(
                        book = book,
                        viewModel = viewModel,
                        popBackStack = onBackClick,
                        createApplication = { showRequestConfirmationDialog = true },
                        inSearch = inSearch,
                        navigateToBrowsePhoto = navigateToBrowsePhoto,
                        isMyBook = isMyBook,
                    )
                }
            }

            is BookDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Произошла ошибка",
                        style = MaterialTheme.typography.labelMedium,
                        color = colorResource(id = R.color.secondary_text),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.BookDetailsContent(
    book: Book,
    viewModel: BookDetailsViewModel,
    popBackStack: () -> Unit,
    createApplication: () -> Unit,
    navigateToBrowsePhoto: (imageUrl: String) -> Unit,
    inSearch: Boolean,
    isMyBook: Boolean,
) {
    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(book.isFavorite) {
        isFavorite = book.isFavorite
    }

    if (inSearch) Spacer(modifier = Modifier.statusBarsPadding())

    Spacer(modifier = Modifier.height(12.dp))

    Row(Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(28.dp)
                .clip(CircleShape)
                .clickable(onClick = popBackStack),
            tint = colorResource(id = R.color.text)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (book.imageUrls.isEmpty()) {
//        AsyncImage(
//            model = null,
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(colorResource(id = R.color.lightest))
//                .height(450.dp),
//            contentScale = ContentScale.FillHeight
//        )
        Text(
            text = "Произошла ошибка загрузки изображения",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.text)
        )
    } else {
        PhotoGrid(
            photos = book.imageUrls,
            onClick = navigateToBrowsePhoto,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.lightest))
                .height(450.dp),
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Title and author
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = book.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
                color = colorResource(id = R.color.text)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.author,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.secondary_text),
                modifier = Modifier.padding(horizontal = 12.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (!isMyBook) {
            IconButton(
                onClick = {
                    viewModel.onFavorite(book.id, isFavorite.not()) {}
                    isFavorite = isFavorite.not()
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                if (isFavorite) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = colorResource(id = R.color.text),
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Состояние",
        style = MaterialTheme.typography.labelSmall,
        color = colorResource(id = R.color.secondary_text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = book.condition,
        style = MaterialTheme.typography.labelMedium,
        color = colorResource(id = R.color.text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Описание от владельца",
        style = MaterialTheme.typography.labelSmall,
        color = colorResource(id = R.color.secondary_text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = book.description,
        style = MaterialTheme.typography.labelMedium,
        color = colorResource(id = R.color.text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    book.genre.takeIf { it.isNotBlank() }?.let { genre ->
        Text(
            text = "Жанр",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = genre,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }

    book.cover.takeIf { it.isNotBlank() }?.let { genre ->
        Text(
            text = "Переплёт",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = genre,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    book.year.takeIf { (it ?: 0) > 0 }?.let { year ->
        Text(
            text = "Опубликована в $year году",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    book.pages.takeIf { (it ?: 0) > 0 }?.let { pages ->
        Text(
            text = "Страницы",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = pages.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }

    book.locationName?.let { name ->
        book.locationAddress?.let { address ->
            Text(
                text = "Адрес",
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.secondary_text),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = address,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    book.username?.let { username ->
        Text(
            text = "Продавец",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )

        Text(
            text = username,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    book.summary.takeIf { it.isNotBlank() && it != "." }?.let { summary ->
        Text(
            text = "Краткое содержание",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        MarkdownView(
            markdown = summary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }

    book.quote.takeIf { it.isNotBlank() }?.let { quote ->
        Text(
            text = "Цитата",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Text(
            text = quote,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
    }

    if (!isMyBook) {
        CustomButton(
            text = "Забронировать",
            onCLick = {
                createApplication()
            },
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
    }

    if (inSearch) {
        Spacer(modifier = Modifier.navigationBarsPadding())
    } else {
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun MarkdownView(markdown: String, modifier: Modifier = Modifier) {
    val html = remember(markdown) {
        // Use a basic markdown to HTML converter here
        // This is very simplified - for real usage, use a proper library
        markdown.replace("**", "<b>", true)
            .replace("**", "</b>", true)
            .replace("*", "<i>", true)
            .replace("*", "</i>", true)
            .replace("# ", "<h1>", true)
            .replace("\n", "<br>", true)
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextColor(context.getColor(R.color.text))
            }
        },
        update = { textView ->
            textView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}

@Composable
private fun PhotoGrid(
    photos: List<String>,
    onClick: (imageUrl: String) -> Unit,
    modifier: Modifier
) {
    val mainPhotoModifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
    val mainPhotoUrl = photos.first()
    AsyncImage(
        modifier = mainPhotoModifier.clickable {
            onClick(mainPhotoUrl)
        },
        model = mainPhotoUrl,
        contentDescription = null,
    )

    if (photos.size > 1) {
        val photosWithoutFirst = photos.toMutableList().apply { removeAt(0) }
        SecondaryPhotosRow(photos = photosWithoutFirst, onClick = onClick)
    }
}

@Composable
private fun SecondaryPhotosRow(photos: List<String>, onClick: (String) -> Unit) {
    LazyRow(modifier = Modifier
        .height(120.dp)
        .fillMaxWidth()) {
        items(photos) { currentPhoto ->
            val photoModifier = Modifier
                .fillMaxHeight()
                .padding(4.dp)
            AsyncImage(
                modifier = photoModifier.clickable {
                    onClick(currentPhoto)
                },
                model = currentPhoto,
                contentDescription = null
            )
        }
    }
}
