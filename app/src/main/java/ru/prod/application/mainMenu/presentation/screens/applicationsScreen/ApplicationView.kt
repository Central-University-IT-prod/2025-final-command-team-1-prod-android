package ru.prod.application.mainMenu.presentation.screens.applicationsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.AsyncImage
import ru.prod.application.R
import ru.prod.application.core.presentation.customComponents.buttons.CustomTinyButton
import ru.prod.application.utils.Book

@Composable
fun ApplicationView(book: Book, showGetConfirmation: () -> Unit, showDeleteConfirmation: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = colorResource(R.color.secondary_background),
                shape = RoundedCornerShape(10.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isError by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(120.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = book.imageUrls.getOrNull(0),
                contentDescription = null,
                onError = { isError = true },
                contentScale = ContentScale.Crop
            )

            if (isError) {
                Image(
                    imageVector = Icons.Default.Face,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = book.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(id = R.color.text)
            )
            Text(
                text = book.author,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(id = R.color.text)
            )
        }

        IconButton(
            onClick = showGetConfirmation,
            modifier = Modifier.background(
                color = Color.Green.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color.Green
            )
        }

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = showDeleteConfirmation,
            modifier = Modifier.background(
                color = Color.Red.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = Color.Red
            )
        }

        Spacer(Modifier.width(12.dp))
    }
}

@Composable
@Preview
fun ApplicationViewPreview() {
    ApplicationView(
        book = Book(
            id = 0,
            imageUrls = listOf(),
            name = "Name",
            description = "dwdawd",
            summary = "",
            author = "",
            cover = "",
            isFavorite = false,
            condition = "",
            genre = "",
            userEmail = "",
            year = null,
            pages = null,
            quote = "",
            username = "",
            locationAddress = "",
            locationName = ""
        ),
        showGetConfirmation = {},
        showDeleteConfirmation = {}
    )
}