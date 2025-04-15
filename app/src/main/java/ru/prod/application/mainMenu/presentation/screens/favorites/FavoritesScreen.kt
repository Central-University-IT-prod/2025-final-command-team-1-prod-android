package ru.prod.application.mainMenu.presentation.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.components.BookItem
import ru.prod.application.mainMenu.presentation.components.SkeletonLoader

@Composable
fun FavoritesScreen(onBookClicked: (id: Int) -> Unit, viewModel: FavoritesViewModel) {

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Избранное",
                style = MaterialTheme.typography.displayLarge,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        viewModel.books?.let { booksSafe ->
            if (booksSafe.isNotEmpty()) {
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
                            onBookClick = { onBookClicked(book1.id) }
                        )

                        book2?.let { book2Safe ->
                            BookItem(
                                book = book2Safe,
                                onBookClick = { onBookClicked(book2Safe.id) }
                            )
                        } ?: Spacer(modifier = Modifier.width(185.dp))
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "В избранном пока пусто",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                        )
                    }
                }
            }
        } ?: items(5) {
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

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
