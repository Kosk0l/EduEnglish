package com.example.eduenglish.NavFiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eduenglish.ViewModels.Card.CardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardVisual(
    cardId: Int,
    navController: NavHostController
) {

    val viewModel: CardViewModel = viewModel()
    val cardFlow = remember(cardId) { viewModel.getCardById(cardId) }
    val card by cardFlow.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    //====================================================================================================

    card?.let {
        var english by remember { mutableStateOf(it.english) }
        var russian by remember { mutableStateOf(it.russian) }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 20.dp, vertical = 30.dp)
        ) {

            // Ориентир
            Text("Настройки карточки ${it.english}",
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.headlineMedium
            )

            // Кнопка возврата
            Button(
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.surface
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {navController.popBackStack()}
            ) {
                Text("Вернуться назад")
            }

            // Доработать цвет
            // окно front
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    focusedLabelColor = MaterialTheme.colorScheme.surface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.surface,
                ),
                value = english,
                onValueChange = { english = it },
                label = {
                    Text("English",
                        color = MaterialTheme.colorScheme.surface)
                }
            )

            // Доработать цвет
            // окно back
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    focusedLabelColor = MaterialTheme.colorScheme.surface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.surface,
                ),
                value = russian,
                onValueChange = { russian = it },
                label = {
                    Text("Russian",
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            )

            // Сохранить
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
                    viewModel.updateCard(
                        it.copy(
                            english = english,
                            russian = russian,
                            )
                    )
                }
            ) {
                Text("Сохранить изменения")
            }

            // удалить карточку
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
                    coroutineScope.launch {
                        viewModel.deleteCard(it)
                        navController.popBackStack()
                    }
                }
            ) {
                Text("Удалить карточку")
            }

        }// Column

    }// card

    //====================================================================================================

}