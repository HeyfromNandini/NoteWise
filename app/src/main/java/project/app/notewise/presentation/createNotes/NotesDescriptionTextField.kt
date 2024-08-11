package project.app.notewise.presentation.createNotes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorColors
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDescriptionField(
    modifier: Modifier = Modifier,
    isFocused: MutableState<Boolean> = mutableStateOf(false),
    textState: RichTextState = rememberRichTextState(),
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        snapshotFlow { isFocused.value }
            .collect { focused ->
                if (focused) {
                    focusRequester.requestFocus()
                } else {
                    focusRequester.freeFocus()
                }
            }
    }
    SelectionContainer {
        RichTextEditor(
            state = textState,
            colors = RichTextEditorDefaults.richTextEditorColors(
                textColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                },
            placeholder = {
                Text("Enter note description...")
            },
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle done action
                }
            ),
            maxLines = Int.MAX_VALUE,
            minLines = 200,
        )
    }
}

@Composable
fun NoteTitleField(
    noteTitle: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = noteTitle,
        onValueChange = { newText ->
            onTitleChange(newText)
        },
        placeholder = {
            Text(
                "Enter note Title...",
                fontSize = 18.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        textStyle = TextStyle(
            fontSize = 30.sp
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // Adjust keyboard action as needed
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Handle done action
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun NoteAIGenerateField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { newText ->
            onValueChange(newText)
        },
        placeholder = { Text("Enter note Title...") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        textStyle = TextStyle(
            fontSize = 30.sp
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // Adjust keyboard action as needed
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Handle done action
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(16.dp)
    )
}
