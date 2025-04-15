package ru.prod.application.mainMenu.presentation.screens.addBookScreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.prod.application.R
import ru.prod.application.core.presentation.customComponents.buttons.CustomButton
import ru.prod.application.core.presentation.customComponents.buttons.CustomTinyButton
import ru.prod.application.core.presentation.customComponents.inputs.CustomTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddBookScreenView(
    popBackStack: () -> Unit,
    navigateToMainMenu: () -> Unit,
    viewModel: AddBookScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val bookName = viewModel.bookName.collectAsStateWithLifecycle()
    val bookAuthor = viewModel.bookAuthor.collectAsStateWithLifecycle()
    val description = viewModel.description.collectAsStateWithLifecycle()
    val selectedLocationId = viewModel.selectedLocationId.collectAsStateWithLifecycle()
    val selectedCondition = viewModel.selectedCondition.collectAsStateWithLifecycle()
    val availableLocations = viewModel.availableLocations.collectAsStateWithLifecycle()
    val images = viewModel.images.collectAsStateWithLifecycle()
    val dropdownExpanded = viewModel.dropdownExpanded.collectAsStateWithLifecycle()
    val addButtonAvailable = viewModel.addButtonAvailable.collectAsStateWithLifecycle()


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val filePath = getRealPathFromURI(context, it)
                filePath?.let { path ->
                    val fileSize =
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            inputStream.available().toLong()
                        } ?: 0
                    if (fileSize <= 1 * 1024 * 1024) { // 1 МБ в байтах
                        viewModel.addImage(path)
                    } else {
                        Toast.makeText(
                            context,
                            "Размер изображения должен быть менее 1 МБ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    )
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if (5 - images.value.size > 1) {
                5 - images.value.size
            } else {
                2
            }
        ),
        onResult = { uris ->
            uris.forEach { uri ->
                val filePath = getRealPathFromURI(context, uri)
                filePath?.let { path ->
                    val fileSize =
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            inputStream.available().toLong()
                        } ?: 0
                    if (fileSize <= 5 * 1024 * 1024) { // 5 МБ в байтах
                        viewModel.addImage(path)
                    } else {
                        Toast.makeText(
                            context,
                            "Размер изображения должен быть менее 5 МБ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    )

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
            .clickableWithoutIndication { focusManager.clearFocus() },
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(
            modifier = Modifier
                .statusBarsPadding()
                .padding(12.dp)
        )

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
                text = "Добавление книги",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = bookName.value,
            onValueChange = viewModel::setBookName,
            placeholder = "Название книги",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = bookAuthor.value,
            onValueChange = viewModel::setBookAuthor,
            placeholder = "Автор",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        CustomTextField(
            value = viewModel.year,
            onValueChange = viewModel::setYear,
            placeholder = "Год",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = viewModel.pages,
            onValueChange = viewModel::setPages,
            placeholder = "Кол-во страниц",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = description.value,
            onValueChange = viewModel::setDescription,
            placeholder = "Описание объявления",
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .heightIn(min = 150.dp)
        )

        availableLocations.value?.let { availableLocationsSafe ->
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Доступные локации",
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.secondary_text),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(Modifier.padding(horizontal = 12.dp)) {
                CustomTinyButton(
                    text = if (selectedLocationId.value >= 0) availableLocationsSafe.find { it.id == selectedLocationId.value }?.name
                        ?: "Выберите локацию"
                    else "Выберите локацию",
                    contentColor = colorResource(id = R.color.text),
                    containerColor = colorResource(id = R.color.secondary_background),
                    onCLick = { viewModel.setDropdownExpanded(true) }
                )

                DropdownMenu(
                    modifier = Modifier.background(colorResource(id = R.color.secondary_background)),
                    expanded = dropdownExpanded.value,
                    onDismissRequest = { viewModel.setDropdownExpanded(false) }
                ) {
                    availableLocationsSafe.forEach {
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colorResource(id = R.color.text),
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                    Text(
                                        text = "${it.city}, ${it.address}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colorResource(id = R.color.secondary_text),
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                }
                            },
                            onClick = { viewModel.setSelectedLocationId(it.id) })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Состояние книги",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        ContextualFlowRow(
            itemCount = viewModel.conditions.size,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = viewModel.conditions[it],
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.secondary_background))
                    .clickable { viewModel.setSelectedCondition(viewModel.conditions[it]) }
                    .border(
                        border = BorderStroke(
                            1.dp,
                            if (viewModel.conditions[it] == selectedCondition.value) {
                                colorResource(id = R.color.darkest)
                            } else {
                                Color.Transparent
                            }
                        ),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Переплет",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ContextualFlowRow(
            itemCount = viewModel.covers.size,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = viewModel.covers[it],
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.secondary_background))
                    .clickable { viewModel.setSelectedCover(viewModel.covers[it]) }
                    .border(
                        border = BorderStroke(
                            1.dp,
                            if (viewModel.covers[it] == viewModel.selectedCover) {
                                colorResource(id = R.color.darkest)
                            } else {
                                Color.Transparent
                            }
                        ),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Жанр книги",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        ContextualFlowRow(
            itemCount = viewModel.genres.size,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = viewModel.genres[it],
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.text),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.secondary_background))
                    .clickable { viewModel.setSelectedGenre(viewModel.genres[it]) }
                    .border(
                        border = BorderStroke(
                            1.dp,
                            if (viewModel.genres[it] == viewModel.selectedGenre) {
                                colorResource(id = R.color.darkest)
                            } else {
                                Color.Transparent
                            }
                        ),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Изображения",
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(id = R.color.secondary_text),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTinyButton(
            text = "Изображение",
            iconPainter = painterResource(id = R.drawable.plus_icon),
            contentColor = colorResource(id = R.color.text),
            containerColor = colorResource(id = R.color.secondary_background),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            if (5 - images.value.size > 1) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(images.value) { index, item ->
                Box {
                    AsyncImage(
                        model = item, contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.removeImageByIndex(index) }
                            .background(colorResource(id = R.color.black).copy(alpha = 0.8f))
                            .padding(2.dp),
                        tint = colorResource(id = R.color.white)
                    )
                }
            }
        }

        CustomButton(
            text = "Выставить объявление",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            isLoading = viewModel.isLoading,
            isEnabled = addButtonAvailable.value
        ) {
            viewModel.addBook(navigateToMainMenu)
        }


        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(12.dp)
        )
    }
}

private fun Modifier.clickableWithoutIndication(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}
