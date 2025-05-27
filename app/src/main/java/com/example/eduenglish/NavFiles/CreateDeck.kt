package com.example.eduenglish.NavFiles

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import com.example.eduenglish.Room.AppDataBase
import com.example.eduenglish.Room.Table.Deck
import com.example.eduenglish.Routes.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity", "RestrictedApi")
@Composable
fun CreateDeck(
    navController: NavController
) {
    val context = LocalContext.current
    var deckName by remember { mutableStateOf("") }
    val db = remember { AppDataBase.getInstance(context) }
    val scope = rememberCoroutineScope()

    //====================================================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {

        // Создать колоду
        Text("Создать новую колоду",
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineMedium
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
                navController.navigate(Routes.Home.route)
            }
        ) {
            Text("Вернуться Назад",
                color = MaterialTheme.colorScheme.surface
            )
        }

        // Ввод названия
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth(),
            value = deckName,
            onValueChange = { deckName = it },
            label = {
                Text(
                    "Название колоды",
                    color = MaterialTheme.colorScheme.surface
                )
            },
        )

        // Сохранение
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
                if (deckName.isNotBlank()) {
                    scope.launch(Dispatchers.IO) {
                        db.deckDao().insert(Deck(name = deckName))
                        withContext(Dispatchers.Main) {
                            navController.navigate(Routes.Home.route)
                        }
                    }
                }
            }
        ) {
            Text("Сохранить",
                color = MaterialTheme.colorScheme.surface)
        }
    }

    //====================================================================================================
}