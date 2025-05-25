package com.example.eduenglish.NavFiles

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eduenglish.ViewModels.Other.StudyViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider

@Composable
fun StudyScreen(
    navController: NavController,
    deckId: Int,
    studyViewModel: StudyViewModel = getStudyViewModel(deckId)
) {


    // карточки
    val cardState by studyViewModel.currentCard.collectAsState()
    var showAnswer by remember { mutableStateOf(false) }
    var lastColor by remember { mutableStateOf(Color.Gray) }

    LaunchedEffect(deckId) {
        studyViewModel.loadCardsForDeck(deckId)
    }

    //====================================================================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cardState == null) {
                Text(
                    "Изучение завершено!",
                    color = MaterialTheme.colorScheme.surface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Вернуться назад")
                }

            } else {

                // Front
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Front: ${cardState?.english}",
                        color = MaterialTheme.colorScheme.surface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back
                if (showAnswer) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp,  MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back: ${cardState?.russian}",
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }

        }

        if (cardState != null) {
            Column(
                modifier = Modifier.padding(bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showAnswer) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                studyViewModel.onAnswer(StudyViewModel.AnswerQuality.BAD)
                                showAnswer = false
                                lastColor = Color.Red
                            },
                            border = BorderStroke(2.dp, Color.Red),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red,
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Плохо")
                        }

                        OutlinedButton(
                            onClick = {
                                studyViewModel.onAnswer(StudyViewModel.AnswerQuality.NORMAL)
                                showAnswer = false
                                lastColor = Color(0xFFFFA500)
                            },
                            border = BorderStroke(2.dp, Color(0xFFFFA500)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFFFA500),
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Нормально")
                        }

                        OutlinedButton(
                            onClick = {
                                studyViewModel.onAnswer(StudyViewModel.AnswerQuality.GOOD)
                                showAnswer = false
                                lastColor = Color.Green
                            },
                            border = BorderStroke(2.dp, Color.Green),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Green,
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Хорошо")
                        }
                    }
                } else {
                    Button(
                        onClick = { showAnswer = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text("Показать ответ")
                    }
                }
            }
        }
    }

}

@Composable
private fun getStudyViewModel(deckId: Int): StudyViewModel {
    val owner = LocalViewModelStoreOwner.current
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    return remember(deckId) {
        ViewModelProvider(owner!!, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return StudyViewModel(app) as T
            }
        }).get(StudyViewModel::class.java)
    }
}