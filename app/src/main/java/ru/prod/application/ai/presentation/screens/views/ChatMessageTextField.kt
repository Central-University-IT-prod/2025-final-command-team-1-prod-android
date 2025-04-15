package ru.prod.application.ai.presentation.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.utils.general.LoadingState

@Composable
fun ChatMessageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    send: () -> Unit,
    loadingState: LoadingState,
    isEnabled: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.secondary_background))
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.message),
                    color = colorResource(id = R.color.secondary_text),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = APP_DEFAULT_FONT_FAMILY
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(id = R.color.secondary_text),
                focusedTextColor = colorResource(id = R.color.text),
                errorTextColor = colorResource(id = R.color.text),
                cursorColor = colorResource(id = R.color.darkest),
                focusedContainerColor = colorResource(id = R.color.secondary_background),
                unfocusedContainerColor = colorResource(id = R.color.secondary_background),
                errorContainerColor = colorResource(id = R.color.secondary_background),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedPlaceholderColor = colorResource(id = R.color.secondary_text),
                focusedPlaceholderColor = colorResource(id = R.color.secondary_text),
                errorPlaceholderColor = colorResource(id = R.color.secondary_text)
            ),
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                fontWeight = FontWeight.Medium
            ),
            shape = RectangleShape,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
        )

        if (loadingState == LoadingState.LOADING) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.darkest),
                trackColor = Color.Transparent,
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp),
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right_double),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.darkest).copy(if (isEnabled) 1f else 0.5f))
                    .clickable(onClick = send, enabled = isEnabled)
                    .padding(4.dp),
                tint = colorResource(id = R.color.white)
            )
        }
    }
}