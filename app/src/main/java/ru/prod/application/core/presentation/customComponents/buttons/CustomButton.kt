package ru.prod.application.core.presentation.customComponents.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

// View стандартной кнопки
@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(id = R.color.darkest),
    contentColor: Color = colorResource(id = R.color.white),
    iconPainter: Painter? = null,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onCLick: () -> Unit
) {
    Button(
        onClick = onCLick,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        enabled = isEnabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
    ) {
        if (!isLoading) {
            if (iconPainter != null) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        } else {
            CircularProgressIndicator(
                color = contentColor,
                trackColor = Color.Transparent,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        }
    }
}