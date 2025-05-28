package com.example.eduenglish.NavFiles

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eduenglish.ViewModels.Deck.DeckViewModel
import com.example.eduenglish.ViewModels.Deck.DeckViewModelFactory
import com.example.eduenglish.Routes.Routes
import java.util.Collections.emptyList

@Composable
fun Home(
    navController: NavController
) {

    // Переменные под deck-Room
    val context = LocalContext.current.applicationContext as Application
    val factory = remember { DeckViewModelFactory(context) }
    val viewModel: DeckViewModel = viewModel(factory = factory)

    val decks by viewModel.allDecks.collectAsState(initial = emptyList())

    //====================================================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {

        Text(
            text = "Home",
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            onClick = {navController.navigate(Routes.CreateDeck.route)},
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Create Deck")
        }


        decks.forEach { deck ->
            val learnableCount by viewModel.getLearnableCardCount(deck.id).collectAsState(initial = 0)
            val dueCount by viewModel.getDueCardCount(deck.id).collectAsState(initial = 0)

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
                    .clickable { navController.navigate(Routes.DeckVisual.createRoute(deck.id)) },
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Deck name: ${deck.name}",
                        color = MaterialTheme.colorScheme.surface
                    )

                    Text(
                        text = "Всего: $learnableCount | Нужно повторить: $dueCount ",
                        color = MaterialTheme.colorScheme.surface
                    )

                    Button(
                        modifier = Modifier.padding(top = 8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        onClick = { navController.navigate(Routes.StudyScreen.createRoute(deck.id)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text(" Начать обучение")
                    }

                }
            }
        }// decks

    }// Column
}
