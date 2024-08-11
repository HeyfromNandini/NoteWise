package project.app.notewise.presentation.aiChat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import project.app.notewise.baseUI.theme.utils.ErrorHandler
import project.app.notewise.domain.models.AIChats
import project.app.notewise.domain.models.Conversations
import project.app.notewise.domain.models.UserNotes
import project.app.notewise.presentation.createNotes.CreateNoteState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    viewModel: AIChatViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val state = rememberCollapsingToolbarScaffoldState()
    val userId = viewModel.userId.collectAsState()
    var allChats by remember { mutableStateOf<List<AIChats>?>(null) }
    var userChats by remember { mutableStateOf<List<AIChats>?>(null) }

    val createNoteState by viewModel.createNoteState.collectAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(createNoteState) {
        when (createNoteState) {
            is CreateNoteState.Error -> {
                errorMessage = (createNoteState as CreateNoteState.Error).message
            }

            else -> Unit
        }
    }

    JetFirestore(path = {
        collection("AIChat")
    }, onRealtimeCollectionFetch = { values, _ ->
        allChats = values?.getListOfObjects()
        println("All notes: $allChats")
    }) {
        userChats = allChats?.filter {
            it.userId == userId.value
        }
    }

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            CollapsingToolbar(
                modifier = Modifier.fillMaxWidth(),
                collapsingToolbarState = state.toolbarState,
                content = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Chat with your Notes",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                ChatsList(
                    chats = userChats?.flatMap { it.conversations } ?: emptyList(),
                    state = createNoteState,
                    Modifier.weight(1f)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 4.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ChatInputField(
                    message = viewModel.message,
                    onMessageChange = { viewModel.message = it },
                    onSendClick = {
                        viewModel.searchNotes(viewModel.message)
                    }
                )
            }
        }

        ErrorHandler(
            errorMessage = errorMessage,
            onDismiss = { errorMessage = null },
            coroutineScope = coroutineScope
        )
    }
}

@Composable
fun ChatsList(
    chats: List<Conversations>,
    state: CreateNoteState,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        if (chats.isNotEmpty()) {
            listState.scrollToItem(chats.size - 1)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        state = listState,
        contentPadding = PaddingValues(
            bottom = 90.dp
        )
    ) {
        items(chats) { chat ->
            ChatItem(chat = chat, state)
        }
        if (state == CreateNoteState.Loading) {
            item {
                Text(
                    "AI is searching for notes...",
                    color = MaterialTheme.colorScheme.primary
                )
                LinearProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(chat: Conversations, state: CreateNoteState) {
    val textState = rememberRichTextState()
    textState.setMarkdown(chat.answer)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = chat.question,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))

        RichTextEditor(
            state = textState,
            colors = RichTextEditorDefaults.richTextEditorColors(
                textColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier,
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
            readOnly = true
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatTimestamp(chat.timestamp),
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputField(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onSendClick) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(-45f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}






