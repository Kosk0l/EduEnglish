package com.example.eduenglish.NavFiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduenglish.Room.AppDataBase
import com.example.eduenglish.Routes.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(
    deckId: Int,
    navController: NavHostController
) {

    val context = LocalContext.current
    val db = remember { AppDataBase.getInstance(context) }

    var english by remember { mutableStateOf("") }
    var russian by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    //====================================================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ){

        // Создать карточку
        Text("Добавить карточку",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.surface
        )

        // Вернуться назад
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
                navController.navigate(Routes.DeckVisual.createRoute(deckId))
            }
        ) {
            Text("Вернуться Назад",
                color = MaterialTheme.colorScheme.surface
            )
        }

        // Ввод front
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth(),
            value = english,
            onValueChange = { english = it },
            label = {
                Text("Front",
                    color = MaterialTheme.colorScheme.surface)
            },
        )

        // Ввод Back
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth(),
            value = russian,
            onValueChange = { russian = it },
            label = {
                Text("Back",
                    color = MaterialTheme.colorScheme.surface
                )
            }

        )

        // Сохранение
        Button(
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            onClick = {
                if (english.isNotBlank() && russian.isNotBlank()) {

                    coroutineScope.launch {
                        val newCard = com.example.eduenglish.Room.Table.Card(
                            deckId = deckId,
                            english = english,
                            russian = russian,
                        )
                        db.cardDao().insert(newCard)
                        navController.popBackStack()
                    }
                }
            },
        ) {
            Text("Сохранить",
                color = MaterialTheme.colorScheme.surface)
        }

    }

    //====================================================================================================
}
