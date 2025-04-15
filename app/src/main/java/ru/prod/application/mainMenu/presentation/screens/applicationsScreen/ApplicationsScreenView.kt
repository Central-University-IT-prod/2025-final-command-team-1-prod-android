package ru.prod.application.mainMenu.presentation.screens.applicationsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ru.prod.application.mainMenu.presentation.components.SkeletonLoader

@Composable
fun ApplicationsScreenView(viewModel: ApplicationsScreenViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Брони",
                style = MaterialTheme.typography.displayLarge,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        viewModel.books?.let { booksSafe ->
            if (booksSafe.isNotEmpty()) {
                items(booksSafe) {
                    ApplicationView(
                        book = it,
                        showDeleteConfirmation = {
                            viewModel.bookingId = it.id
                            viewModel.email = it.userEmail
                            viewModel.showDeleteConfirmationAlert = true
                        },
                        showGetConfirmation = {
                            viewModel.bookingId = it.id
                            viewModel.email = it.userEmail
                            viewModel.showGetConfirmationAlert = true
                        }
                    )
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
                            text = "Заявок нет",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(id = R.color.secondary_text),
                        )
                    }
                }
            }
        } ?: items(5) {
            SkeletonLoader(
                Modifier
                    .padding(horizontal = 12.dp)
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        item {
            Spacer(Modifier.width(12.dp))
        }
    }

    if (viewModel.showGetConfirmationAlert) {
        GetBookConfirmationAlert(
            onConfirm = { count, feedback ->
                viewModel.confirmGetBook(count, feedback, viewModel.email)
                viewModel.showGetConfirmationAlert = false
            },
            onDismissRequest = { viewModel.showGetConfirmationAlert = false }
        )
    }

    if (viewModel.showDeleteConfirmationAlert) {
        DeleteBookConfirmationAlert(
            onConfirm = { count, feedback ->
                viewModel.confirmDeleteBook(count, feedback, viewModel.email)
                viewModel.showDeleteConfirmationAlert = false
            },
            onDismissRequest = { viewModel.showDeleteConfirmationAlert = false }
        )
    }
}