package ru.prod.application.mainMenu.presentation.screens.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.components.BookItem
import ru.prod.application.mainMenu.presentation.components.SkeletonLoader

@Composable
fun ProfileScreenView(
    navigateToAIChat: () -> Unit,
    navigateToReviews: (String) -> Unit,
    onBookClick: (Int) -> Unit,
    viewModel: ProfileScreenViewModel,
) {
    val username = viewModel.username.collectAsStateWithLifecycle()
    val books = viewModel.books.collectAsStateWithLifecycle()
    val rating = viewModel.rating.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.log_out),
                    tint = colorResource(id = R.color.text),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .clickable(onClick = { viewModel.logout() })
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (username.value?.length == 0) {
                viewModel.logout()
                return@item
            }
            username.value?.let { usernameSafe ->
                Column(Modifier.padding(horizontal = 12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.light))
                    ) {
                        if (usernameSafe.isNotBlank()) {
                            Text(
                                text = usernameSafe[0].toString(),
                                fontSize = 34.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight.Black,
                                color = colorResource(id = R.color.text),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = usernameSafe,
                        style = MaterialTheme.typography.displayLarge,
                        color = colorResource(id = R.color.text)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            Modifier
                                .clip(CircleShape)
                                .background(colorResource(id = R.color.yellow).copy(0.2f))
                                .clickable { navigateToReviews(username.value!!) }
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = colorResource(id = R.color.yellow)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = rating.value.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = colorResource(id = R.color.text)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Отзывы",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.blue),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { navigateToReviews(username.value!!) }
                        )
                    }
                }
            } ?: SkeletonLoader(
                Modifier
                    .width(100.dp)
                    .height(17.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(CircleShape), thickness = 1.dp,
                color = colorResource(id = R.color.secondary_text)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Мои книги",
                style = MaterialTheme.typography.displayLarge,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        books.value?.let { booksSafe ->
            if (booksSafe.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Книг пока нет",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                        )
                    }
                }
            } else {
                items(booksSafe.size / 2 + booksSafe.size % 2) { index ->
                    val book1 = booksSafe[index * 2]
                    val book2 =
                        if (index * 2 + 1 < booksSafe.size) booksSafe[index * 2 + 1] else null

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BookItem(
                            book = book1,
                            onBookClick = { onBookClick(book1.id) }
                        )

                        book2?.let { book2Safe ->
                            BookItem(
                                book = book2Safe,
                                onBookClick = { onBookClick(book2Safe.id) }
                            )
                        } ?: Spacer(modifier = Modifier.width(185.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } ?: items(3) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SkeletonLoader(
                    Modifier
                        .width(185.dp)
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                SkeletonLoader(
                    Modifier
                        .width(185.dp)
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}