package com.example.eduenglish.NavFiles

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.eduenglish.ViewModels.Other.SearchViewModel


@Composable
fun Search(viewModel: SearchViewModel) {
    val decks by viewModel.decks.collectAsState()
    val loadingId by viewModel.loadingDeckId.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        Text(
            text = "Search",
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Колоды, одобренные модерацией:")

        LazyColumn {
            items(decks) { deck ->
                Log.d("SearchUI", "Отображение колоды: ${deck.name}")

                Card(
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.surface
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            deck.name,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            deck.description,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.primary
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                Log.d("SearchUI", "Нажата кнопка скачивания: ${deck.name}")
                                viewModel.downloadDeck(deck)
                            },
                            enabled = loadingId != deck.id
                        ) {
                            Text(if (loadingId == deck.id) "Скачивается..." else "Скачать")
                        }
                    }
                }
            }
        }
    }
}


