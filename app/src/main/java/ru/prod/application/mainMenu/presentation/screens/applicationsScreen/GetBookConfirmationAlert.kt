package ru.prod.application.mainMenu.presentation.screens.applicationsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.presentation.customComponents.buttons.CustomButton
import ru.prod.application.core.presentation.customComponents.inputs.CustomTextField

// Alert с двумя кнопкой
@Composable
fun GetBookConfirmationAlert(
    onConfirm: (Int, String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var score by remember { mutableIntStateOf(0) }
    var feeedback by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.background))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Получение книги",
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Оцените сервис",
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = if (i <= score) colorResource(id = R.color.yellow) else colorResource(
                            id = R.color.secondary_background
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                score = if (score == i) 0 else i
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = feeedback,
                onValueChange = { feeedback = it },
                placeholder = "Отзыв"
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = "Подтвердить получение",
                isEnabled = score > 0 || feeedback.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { onConfirm(score, feeedback) }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Отмена",
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = colorResource(id = R.color.darkest)
            ) { onDismissRequest() }
        }
    }
}