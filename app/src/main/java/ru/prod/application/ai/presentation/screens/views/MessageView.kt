package ru.prod.application.ai.presentation.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.prod.application.R
import ru.prod.application.utils.aiChat.MessageAuthor
import ru.prod.application.utils.aiChat.MessageModel

/* View сообщения в чате с ИИ */
@Composable
fun MessageView(message: MessageModel) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                start = if (message.author == MessageAuthor.AI) 0.dp else 40.dp,
                end = if (message.author == MessageAuthor.USER) 0.dp else 40.dp
            )
    ) {
        Text(
            text = message.text,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.text),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = when (message.author) {
                        MessageAuthor.USER -> colorResource(id = R.color.lighter)
                        MessageAuthor.AI -> colorResource(id = R.color.secondary_background)
                    }
                )
                .padding(8.dp)
                .align(
                    when (message.author) {
                        MessageAuthor.USER -> Alignment.CenterEnd
                        MessageAuthor.AI -> Alignment.CenterStart
                    }
                )
        )
    }
}