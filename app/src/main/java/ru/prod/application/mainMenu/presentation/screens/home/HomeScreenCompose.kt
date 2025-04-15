package ru.prod.application.mainMenu.presentation.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.components.BookItem
import ru.prod.application.mainMenu.presentation.components.SearchBarView
import ru.prod.application.mainMenu.presentation.components.SkeletonLoader
import ru.prod.application.utils.Book

@Composable
fun HomeScreenCompose(
    viewModel: HomeViewModel,
    onBookClick: (Book) -> Unit,
    navigateToSearch: () -> Unit
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val banners = listOf(
        BannerModel(
            "Книжный клуб.rar",
            "Книжки от Александра Шахова",
            Brush.horizontalGradient(
                listOf(
                    colorResource(id = R.color.b1c1),
                    colorResource(id = R.color.b1c2)
                )
            ),
            painterResource(id = R.drawable.dino),
            "https://t.me/point_rar"
        ),
        BannerModel(
            "Книжный клуб ЦУ",
            "Читай книжки за грант",
            Brush.horizontalGradient(
                listOf(
                    colorResource(id = R.color.b2c1),
                    colorResource(id = R.color.b2c2)
                )
            ),
            painterResource(id = R.drawable.cu),
            "https://centraluniversity.ru/"
        ),
        BannerModel(
            "Книжный клуб Алексея Голобурдина",
            "Диджетализируй",
            Brush.horizontalGradient(
                listOf(
                    colorResource(id = R.color.b3c1),
                    colorResource(id = R.color.b3c2)
                )
            ),
            painterResource(id = R.drawable.buisnes),
            "https://t.me/t0digital"
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { banners.size })

    LaunchedEffect(pagerState) {
        while (true) {
            delay(7000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            SearchBarView(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .clickable(onClick = navigateToSearch),
                isEnabled = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                beyondViewportPageCount = 1
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(banners[it].brush)
                        .clickable {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(banners[it].link))
                            context.startActivity(intent)
                        }
                ) {
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(12.dp)
                            .zIndex(1f)
                    ) {
                        Text(
                            text = banners[it].title,
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.white)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = banners[it].subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.white)
                        )
                    }

                    Image(
                        painter = banners[it].image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 8.dp)
                            .widthIn(max = 160.dp)
                            .zIndex(0f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Доступные книги",
                style = MaterialTheme.typography.displayLarge,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        when (uiState) {
            is BookListUiState.Loading -> {
                items(5) {
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

            is BookListUiState.Success -> {
                val books = (uiState as BookListUiState.Success).books
                if (books.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Книг пока нет",
                                style = MaterialTheme.typography.labelMedium,
                                color = colorResource(id = R.color.secondary_text),
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                } else {
                    items(books.size / 2 + books.size % 2) { index ->
                        val book1 = books[index * 2]
                        val book2 =
                            if (index * 2 + 1 < books.size) books[index * 2 + 1] else null

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BookItem(
                                book = book1,
                                onBookClick = { onBookClick(book1) }
                            )

                            book2?.let { book2Safe ->
                                BookItem(
                                    book = book2Safe,
                                    onBookClick = { onBookClick(book2Safe) }
                                )
                            } ?: Spacer(modifier = Modifier.width(185.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            is BookListUiState.Error -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Произошла ошибка",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}
