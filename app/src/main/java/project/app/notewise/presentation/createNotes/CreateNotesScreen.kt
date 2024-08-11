package project.app.notewise.presentation.createNotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import project.app.notewise.baseUI.theme.utils.ErrorHandler
import project.app.notewise.data.createNote.CreateNoteRequest
import project.app.notewise.domain.models.formattingIcons
import project.app.notewise.presentation.loginScreen.AuthState


@Composable
fun CreateNoteScreen(
    paddingValues: PaddingValues,
    viewModel: CreateNotesViewModel
) {
    val textState = rememberRichTextState()
    val keyboardVisible by keyboardAsState()
    val state = rememberCollapsingToolbarScaffoldState()
    val uid = viewModel.userId.collectAsState()
    val createNoteState by viewModel.createNoteState.collectAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(createNoteState) {
        when (createNoteState) {
            is CreateNoteState.Success -> {
                errorMessage = "Note saved successfully"
                delay(500)
                errorMessage = "We're setting up things for you"
            }

            is CreateNoteState.Error -> {
                errorMessage = (createNoteState as CreateNoteState.Error).message
            }

            else -> Unit
        }
    }

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Save",
                        modifier = Modifier.size(24.dp)
                    )
                }
                NoteTitleField(noteTitle = viewModel.title, onTitleChange = {
                    viewModel.title = it
                }, modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        viewModel.isBottomSheetVisible.value = !viewModel.isBottomSheetVisible.value
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.FileUpload,
                            contentDescription = "Save",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NoteDescriptionField(
                    isFocused = viewModel.isDescriptionFocused,
                    textState = textState
                )
            }
            AnimatedVisibility(
                keyboardVisible == Keyboard.Opened,
                enter = slideInHorizontally(initialOffsetX = { -it / 2 }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedVisibility(
                        viewModel.isDescriptionFocused.value,
                        enter = slideInHorizontally(initialOffsetX = { -it / 2 }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
                    ) {
                        LazyRow {
                            items(formattingIcons) {
                                IconButton(
                                    onClick = {
                                        viewModel.currentIcon = it
                                        it.onClick(textState)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = if (it == viewModel.currentIcon)
                                            MaterialTheme.colorScheme.secondaryContainer else
                                            MaterialTheme.colorScheme.background
                                    )
                                ) {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = "",
                                        tint = if (it == viewModel.currentIcon)
                                            MaterialTheme.colorScheme.primary else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (viewModel.isBottomSheetVisible.value) {
            SaveNoteBottomSheet(
                onDismiss = {
                    viewModel.isBottomSheetVisible.value = false
                },
                onSaveNote = { request ->
                    println("Is loading3 called")
                    viewModel.saveNote(
                        CreateNoteRequest(
                            author = request.author,
                            content = textState.toMarkdown(),
                            isEncrypted = false,
                            priority = request.priority,
                            title = request.title,
                            category = request.category,
                            timestamp = request.timestamp,
                            uId = uid.value ?: "",
                        )
                    )
                },
                initialTitle = viewModel.title,
                initialContent = textState.toMarkdown()
            )
        }
    }

    ErrorHandler(
        errorMessage = errorMessage,
        onDismiss = { errorMessage = null },
        coroutineScope = coroutineScope
    )
}