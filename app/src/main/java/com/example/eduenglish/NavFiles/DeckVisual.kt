package com.example.eduenglish.NavFiles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eduenglish.ViewModels.Card.CardViewModel
import com.example.eduenglish.ViewModels.Deck.DeckViewModel
import com.example.eduenglish.Routes.Routes

@Composable
fun DeckVisual(
    navController: NavHostController,
    deckId: Int
) {
    // Вслывающее окно удаления колоды
    var showDialogDelete by remember { mutableStateOf(false) }

    // Настройки Room
    val viewModel: CardViewModel = viewModel()
    val cards by viewModel.getCardsForDeck(deckId).collectAsState(initial = emptyList())
    val deckViewModel: DeckViewModel = viewModel()

    val deck by remember(deckId) {
        deckViewModel.getDeckById(deckId)
    }.collectAsState(initial = null)

    //====================================================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {

        Text(
            "Настройки колоды ${deck?.name}",
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineMedium
        )

        // Возврат назад
        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            onClick = {navController.navigate(Routes.Home.route)},
        ) {
            Text(
                "Вернуться назад",
                color = MaterialTheme.colorScheme.surface
            )
        }

        // Добавить карточку
        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            onClick = {
                navController.navigate(Routes.CreateCard.createRoute(deckId))
            }
        ) {
            Text("Добавить карточку",)
        }

        // Изучение
        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            onClick = { navController.navigate(Routes.StudyScreen.createRoute(deckId)) }
        ) {
            Text("Приступить к изучению",)
        }

        // Удаление
        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            onClick = { showDialogDelete = true }
        ) {
            Text("Удалить Колоду")
        }


        // Колода
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards) { card ->
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
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("CardVisual/${card.id}")
                        },
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "English: ${card.english}",
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            text = "Russian: ${card.russian}",
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }

        // Окно Удаления Колоды
        if (showDialogDelete) {
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.primary,
                onDismissRequest = { showDialogDelete = false },
                title = {
                    Text(
                        "Подтверждение",
                        color = MaterialTheme.colorScheme.surface
                    )
                },
                text = {
                    Text(
                        "Вы уверены, что хотите удалить эту колоду? Это действие необратимо.",
                        color = MaterialTheme.colorScheme.surface
                        )
                },
                confirmButton = {
                    TextButton(
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.surface
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = {
                            deck?.let {
                                deckViewModel.deleteDeckWithCards(deckId)
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Text("Удалить",)
                    }
                },
                dismissButton = {
                    TextButton(
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.surface
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { showDialogDelete = false }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }// Alert окно

    }// Column

}

