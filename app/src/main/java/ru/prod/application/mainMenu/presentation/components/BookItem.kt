package ru.prod.application.mainMenu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.prod.application.R
import ru.prod.application.utils.Book

@Composable
fun BookItem(
    book: Book,
    onBookClick: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .width(185.dp)
                .background(colorResource(id = R.color.secondary_background))
                .clickable(onClick = onBookClick)
        ) {
            // Book cover image
            AsyncImage(
                model = if (book.imageUrls.isNotEmpty()) book.imageUrls[0] else "",
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(185.dp)
                    .background(colorResource(id = R.color.lightest)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = book.name,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = book.author,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.secondary_text),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
private fun BookItemPreview() {
    MaterialTheme {
        BookItem(
            book = Book(
                id = 0,
                imageUrls = listOf(),
                name = "Fifty shades of grey",
                description = "Selling an adult only book!",
                summary = "They had sex",
                author = "E. L. James",
                condition = "Good",
                isFavorite = false,
                genre = "Detective",
                userEmail = "",
                year = 2022,
                pages = 228,
                cover = "Твердый",
                quote = "",
                username = "",
                locationAddress = "",
                locationName = "",
            )
        ) { }
    }
}