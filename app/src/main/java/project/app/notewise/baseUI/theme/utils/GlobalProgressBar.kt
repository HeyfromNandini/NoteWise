package project.app.notewise.baseUI.theme.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import project.app.notewise.presentation.createNotes.CreateNotesViewModel

@Composable
fun GlobalProgressBar(viewModel: CreateNotesViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    println("Is loading $isLoading")

    if (isLoading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colorScheme.background),
            color = Color(0xFF92B36C)
        )
    }
}
