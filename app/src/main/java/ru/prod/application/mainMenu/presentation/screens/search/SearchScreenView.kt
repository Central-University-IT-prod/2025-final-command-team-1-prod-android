package ru.prod.application.mainMenu.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.components.SearchBarView

@Composable
fun SearchScreenView(
    navigateToBookDetails: (Int) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val query = viewModel.query.collectAsStateWithLifecycle()
    val books = viewModel.books.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())

        SearchBarView(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = query.value,
            onValueChange = viewModel::setQuery,
            search = {
                viewModel.search()
                keyboardController?.hide()
            }
        )


        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (isLoading.value) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = colorResource(id = R.color.darkest),
                            trackColor = Color.Transparent,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                }
            } else if (query.value.isBlank()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Введите запрос",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else if (books.value != null && books.value!!.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ничего не найдено",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else {
                books.value?.let { booksSafe ->
                    items(booksSafe) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(id = R.color.secondary_background))
                                .clickable { navigateToBookDetails(it.id) }
                        ) {
                            AsyncImage(
                                model = it.imageUrls,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colorResource(id = R.color.lightest))
                            )

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = colorResource(id = R.color.text)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = it.author,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = colorResource(id = R.color.secondary_text)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}