package ru.prod.application.mainMenu.presentation.screens.reviewsScreen

import android.widget.Space
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.components.SkeletonLoader
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReviewsScreenView(
    popBackStack: () -> Unit,
    username: String,
    viewModel: ReviewsScreenViewModel = hiltViewModel()
) {
    val reviews = viewModel.reviews.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData(username)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Text(
                text = "Отзывы",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        reviews.value?.let { reviewsSafe ->
            if (reviewsSafe.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Отзывов пока нет",
                        style = MaterialTheme.typography.labelMedium,
                        color = colorResource(id = R.color.secondary_text),
                    )
                }
            } else {
                reviewsSafe.forEach {
                    Column(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(id = R.color.secondary_background))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colorResource(id = R.color.light))
                            ) {
                                if (it.reviewer_username.isNotBlank()) {
                                    Text(
                                        text = it.reviewer_username[0].toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = colorResource(id = R.color.text),
                                        modifier = Modifier.align(Alignment.Center)
                                            .offset(y = (-1).dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = it.reviewer_username,
                                style = MaterialTheme.typography.labelMedium,
                                color = colorResource(id = R.color.text),
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            if (it.rating > 0) {
                                Row(
                                    Modifier
                                        .clip(CircleShape)
                                        .background(colorResource(id = R.color.yellow).copy(0.2f))
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
                                        text = it.rating.toString(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colorResource(id = R.color.text)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val formattedDate = remember(it.created_at) {
                            getReadableDate(it.created_at)
                        }
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(id = R.color.secondary_text)
                        )

                        if (it.comment.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = it.comment,
                                style = MaterialTheme.typography.labelMedium,
                                color = colorResource(id = R.color.text)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } ?: Box(
            Modifier
                .fillMaxSize(),
        ) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = colorResource(id = R.color.darkest),
                trackColor = Color.Transparent,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 40.dp)
                    .size(28.dp)
            )
        }
    }
}

fun getReadableDate(dateStr: String): String {
    val zonedDateTime = ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(zonedDateTime)
}
