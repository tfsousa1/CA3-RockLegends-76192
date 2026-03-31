package com.example.ca3rocklegends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ca3rocklegends.ui.theme.CA3RockLegendsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CA3RockLegendsTheme {
                RockLegendsApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockLegendsApp() {

    // This list stores the songs shown on screen.
    // The app starts with 5 songs to match the assignment requirement.
    val songList = remember {
        mutableStateListOf(
            Song("Back In Black", "AC/DC", "1980"),
            Song("Plush", "Stone Temple Pilots", "1992"),
            Song("Sweet Child O' Mine", "Guns N' Roses", "1987"),
            Song("Smells Like Teen Spirit", "Nirvana", "1991"),
            Song("Enter Sandman", "Metallica", "1991")
        )
    }

    // These variables hold the values typed by the user in the form.
    var titleInput by rememberSaveable { mutableStateOf("") }
    var artistInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }

    // This text gives feedback after add or delete actions.
    var message by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Rock Legends Playlist",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Classic rock songs",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Delete songs from the list or add a new one below.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // The list of songs is displayed here.
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(songList) { index, song ->
                    SongCard(
                        song = song,
                        onDeleteClick = {
                            songList.removeAt(index)
                            message = "\"${song.title}\" was deleted."
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Divider separates the list from the form section.
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Add a new song",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Input field for the song title.
            OutlinedTextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                label = { Text("Song Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for the artist name.
            OutlinedTextField(
                value = artistInput,
                onValueChange = { artistInput = it },
                label = { Text("Artist") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for the year.
            OutlinedTextField(
                value = yearInput,
                onValueChange = { yearInput = it },
                label = { Text("Year") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    // Basic validation checks if all 3 fields were filled in.
                    if (titleInput.isNotBlank() && artistInput.isNotBlank() && yearInput.isNotBlank()) {

                        songList.add(
                            Song(
                                title = titleInput,
                                artist = artistInput,
                                year = yearInput
                            )
                        )

                        message = "\"$titleInput\" was added successfully."

                        // Clear the form after a successful add.
                        titleInput = ""
                        artistInput = ""
                        yearInput = ""

                    } else {
                        message = "Please complete all 3 fields."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Song")
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Artist: ${song.artist}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Year: ${song.year}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // This delete button appears beside each item in the list.
            // It uses colour to make the action more visible.
            IconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Song",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRockApp() {
    CA3RockLegendsTheme {
        RockLegendsApp()
    }
}