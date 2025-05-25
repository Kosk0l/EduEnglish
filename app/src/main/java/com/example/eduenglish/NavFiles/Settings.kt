package com.example.eduenglish.NavFiles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eduenglish.ViewModels.Other.SettingsViewModel
import com.example.eduenglish.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {

    val currentTheme by settingsViewModel.theme.collectAsState()
    val email by settingsViewModel.email.collectAsState()
    val emailState = remember { mutableStateOf(email) }

    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()

    //====================================================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {

        Text(
            text = "Settings",
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineMedium
        )

        // Настройки Темы
        AppTheme.entries.forEach { theme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.surface
                    )
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.surface,
                        unselectedColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = theme == currentTheme,
                    onClick = { settingsViewModel.setTheme(theme) },
                    modifier = Modifier,
                )
                Text(text = theme.name,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Уведомления:",
                color = MaterialTheme.colorScheme.surface
            )

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { settingsViewModel.setNotificationsEnabled(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondary
                )

            )
        }

        // Email
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface,
            ),
            value = emailState.value,
            onValueChange = {
                emailState.value = it
                settingsViewModel.saveEmail(it)
            },
            label = { Text("Your email",
                color = MaterialTheme.colorScheme.surface) },
            modifier = Modifier.fillMaxWidth()
        )

        // О приложении
        Text(text = "Версия приложения: 1.0.0",
            color = MaterialTheme.colorScheme.surface)
        Text(text = "Разработчик: Николай Костенко - Студент РТУ МИРЭА ИВБО-12-24",
            color = MaterialTheme.colorScheme.surface)
        Text(text = "© 2025 EduEnglish - Samsung Innovation Campus",
            color = MaterialTheme.colorScheme.surface)

    }// Column
}