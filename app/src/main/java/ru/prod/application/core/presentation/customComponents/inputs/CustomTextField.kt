package ru.prod.application.core.presentation.customComponents.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

// View с кастомной реализацией TextField
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isSecured: Boolean = false,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var isFocused by remember { mutableStateOf(false) }
    var isSecuredContentVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        },
        isError = isError,
        visualTransformation = if (isSecured && !isSecuredContentVisible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = colorResource(id = R.color.text),
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
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                2.dp,
                when {
                    isError -> colorResource(id = R.color.error)
                    isFocused -> colorResource(id = R.color.darkest)
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = MaterialTheme.typography.labelMedium,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            if (isSecured) {
                val image = if (isSecuredContentVisible) {
                    painterResource(id = R.drawable.visible)
                } else {
                    painterResource(id = R.drawable.invisible)
                }
                IconButton(onClick = { isSecuredContentVisible = !isSecuredContentVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = null,
                        tint = if (isError) colorResource(id = R.color.error) else colorResource(id = R.color.secondary_text)
                    )
                }
            }
        },
        keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
        readOnly = readOnly
    )
}