package com.example.task.task


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteApp() {
    var notes by remember {
        mutableStateOf(
            listOf(
                Note(
                    title = "Welcome Note",
                    description = "This is your first note! Tap the + button to add more notes."
                ),
                Note(
                    title = "Sample Note",
                    description = "This is a sample note to demonstrate the app functionality."
                )
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Notes",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (notes.isEmpty()) {
                EmptyNotesState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        NoteCard(
                            note = note,
                            onClick = {
                                selectedNote = note
                                Toast.makeText(
                                    context,
                                    "Selected: ${note.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onEdit = { noteToEdit ->
                                selectedNote = noteToEdit
                                showEditDialog = true
                            },
                            onDelete = { noteToDeleteParam ->
                                noteToDelete = noteToDeleteParam
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { showAddDialog = false },
            onNoteAdded = { newNote ->
                notes = notes + newNote
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedNote != null) {
        EditNoteDialog(
            note = selectedNote!!,
            onDismiss = {
                showEditDialog = false
                selectedNote = null
            },
            onNoteUpdated = { updatedNote ->
                notes = notes.map { if (it.id == updatedNote.id) updatedNote else it }
                showEditDialog = false
                selectedNote = null
            }
        )
    }

    if (showDeleteDialog && noteToDelete != null) {
        DeleteConfirmationDialog(
            note = noteToDelete!!,
            onDismiss = {
                showDeleteDialog = false
                noteToDelete = null
            },
            onConfirmDelete = {
                notes = notes.filter { it.id != noteToDelete!!.id }
                showDeleteDialog = false
                noteToDelete = null
            }
        )
    }
}
