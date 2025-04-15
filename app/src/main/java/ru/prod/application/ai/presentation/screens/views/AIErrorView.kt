package ru.prod.application.ai.presentation.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.prod.application.R

/* View ошибки при запросе к ИИ */
@Composable
fun AIErrorView(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.ai_error),
        style = MaterialTheme.typography.labelMedium,
        color = colorResource(id = R.color.error),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.error).copy(0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}