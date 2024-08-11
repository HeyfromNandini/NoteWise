package project.app.notewise.presentation.homeScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import project.app.notewise.R
import project.app.notewise.domain.models.UserNotes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    paddingValues: PaddingValues
) {
    val state = rememberCollapsingToolbarScaffoldState()
    var userNotes by remember { mutableStateOf<List<UserNotes>?>(null) }
    var backUpUserNotes by remember { mutableStateOf<List<UserNotes>?>(null) }
    var filteredNotes by remember { mutableStateOf<List<UserNotes>?>(null) }
    var priorSelectedPriority by remember { mutableStateOf<String?>(null) }
    var priorSelectedCategory by remember { mutableStateOf<String?>(null) }
    var priorSelectedDate by remember { mutableStateOf<Long?>(null) }
    var allNotes by remember { mutableStateOf<List<UserNotes>?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var filterBottomSheetOpen by remember { mutableStateOf(false) }
    var isFilterApplied by remember { mutableStateOf(false) }
    val categories = listOf("Category1", "Category2") // Define your categories
    val priorities = listOf("High", "Medium", "Low") // Define your priorities

    val userInfo by viewModel.userInfo.collectAsState(initial = listOf())

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    JetFirestore(path = {
        collection("userNotes")
    }, onRealtimeCollectionFetch = { values, _ ->
        allNotes = values?.getListOfObjects()
        println("All notes: $allNotes")
    }) {
        if (userInfo.isNotEmpty()) {
            val tempLocalId = userInfo[0]?.localId ?: ""
            userNotes = allNotes?.filter {
                println("User notes: ${it.uId} && $tempLocalId")
                it.uId == tempLocalId
            }?.sortedByDescending { it.timestamp }
            backUpUserNotes = userNotes
            println("User notes: $userNotes")
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isFilterApplied = true
            filteredNotes = userNotes?.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.priority.contains(searchQuery, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) } ||
                formatTimestamp(it.timestamp).contains(searchQuery, ignoreCase = true)
            }
        } else {
            isFilterApplied = false
            userNotes = backUpUserNotes
        }
    }

    CollapsingToolbarScaffold(
        modifier = modifier,
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notewise),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(65.dp)
                        )
                        Text("NoteWise", style = TextStyle(fontSize = 20.sp))
                    }
                },
                actions = {
                    IconButton(onClick = { filterBottomSheetOpen = !filterBottomSheetOpen }) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(interactionSource, null) {
                    focusManager.clearFocus()
                }
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                shape = RoundedCornerShape(40.dp),

            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    if (isFilterApplied) filteredNotes ?: emptyList() else userNotes ?: emptyList()
                ) { note ->
                    SavedCard(
                        title = note.title,
                        description = note.content,
                        timeStamp = formatTimestamp(note.timestamp)
                    )
                }
            }
        }

        if (filterBottomSheetOpen) {
            FilterBottomSheet(
                onDismiss = { filterBottomSheetOpen = !filterBottomSheetOpen },
                categories = categories,
                priorities = priorities,
                priorSelectedCategory = priorSelectedCategory,
                priorSelectedPriority = priorSelectedPriority,
                priorSelectedCDate = priorSelectedDate,
                onApplyFilters = { selectedFilters ->
                    priorSelectedCategory = selectedFilters["category"] as String?
                    priorSelectedPriority = selectedFilters["priority"] as String?
                    priorSelectedDate = selectedFilters["date"] as Long?
                    isFilterApplied = true
                    filteredNotes = userNotes?.filter { note ->
                        val category = selectedFilters["category"] as String?
                        val isCategoryMatch = category == null || note.category == category
                        isCategoryMatch
                    }
                    filteredNotes = filteredNotes?.filter { note ->
                        val priority = selectedFilters["priority"] as String?
                        val isPriorityMatch = priority == null || note.priority == priority
                        isPriorityMatch
                    }
                    val selectedDate = selectedFilters["date"] as Long?
                    if (selectedDate != null) {
                        filteredNotes = filteredNotes?.filter { note ->
                            note.timestamp == selectedDate
                        }
                    }
                    filteredNotes?.forEach {
                        println("Filtered notes: ${it.content.take(10)} && ${it.priority}")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    categories: List<String>,
    priorities: List<String>,
    priorSelectedCategory: String? = null,
    priorSelectedPriority: String? = null,
    priorSelectedCDate: Long? = null,
    onApplyFilters: (Map<String, Any?>) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategory by remember { mutableStateOf<String?>(priorSelectedCategory) }
    var selectedPriority by remember { mutableStateOf<String?>(priorSelectedPriority) }
    var selectedDate by remember { mutableStateOf<Long?>(priorSelectedCDate) }

    // Date Picker Dialog
    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(context).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.timeInMillis
            }
        }
    }

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(500.dp)

        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column() {
                    Text(
                        "Filters",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    FilterSection(
                        title = "Category",
                        items = categories,
                        selectedItem = selectedCategory,
                        onItemSelected = {
                            selectedCategory = if (it == selectedCategory) {
                                null
                            } else {
                                it
                            }
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    FilterSection(
                        title = "Priority",
                        items = priorities,
                        selectedItem = selectedPriority,
                        onItemSelected = {
                            selectedPriority = if (it == selectedPriority) {
                                null
                            } else {
                                it
                            }
                        })
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Date",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Button(
                        onClick = { datePickerDialog.show() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                                0.7f
                            )
                        )
                    ) {
                        Text(
                            text = if (selectedDate != null) "Date Selected" else "Select Date",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp, end = 10.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                println("Filtered notes: 2 $selectedCategory && $selectedPriority && $selectedDate")
                                onApplyFilters(
                                    mapOf(
                                        "category" to selectedCategory,
                                        "priority" to selectedPriority,
                                        "date" to selectedDate
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    0.7f
                                )
                            )
                        ) {
                            Text("Apply", color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    title: String,
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit
) {
    Column {
        Text(
            title,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                FilterChip(
                    selected = item == selectedItem,
                    onClick = { onItemSelected(item) },
                    label = { Text(item, color = MaterialTheme.colorScheme.onSecondary) }
                )
            }
        }
    }
}


@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipColor = if (selected) Color.Green.copy(0.25f)
    else MaterialTheme.colorScheme.surfaceContainer.copy(0.7f)
    val textColor = if (selected) Color.White else Color.Black

    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        color = chipColor,
        shape = RoundedCornerShape(16.dp),

        ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                label()
            }
        }
    }
}


@Composable
fun SavedCard(
    title: String,
    description: String,
    timeStamp: String,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { onClick() }
            .shadow(
                7.dp,
                RoundedCornerShape(10.dp),
                ambientColor = MaterialTheme.colorScheme.onPrimary
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 14.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = timeStamp,
                style = TextStyle(
                    fontSize = 12.sp,
                ),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f)
            )
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}



