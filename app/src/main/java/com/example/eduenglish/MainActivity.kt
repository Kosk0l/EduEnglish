package com.example.eduenglish

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eduenglish.NavFiles.CardVisual
import com.example.eduenglish.NavFiles.CreateCard
import com.example.eduenglish.NavFiles.CreateDeck
import com.example.eduenglish.NavFiles.DeckVisual
import com.example.eduenglish.NavFiles.Home
import com.example.eduenglish.NavFiles.Search
import com.example.eduenglish.NavFiles.Settings
import com.example.eduenglish.NavFiles.StudyScreen
import com.example.eduenglish.Notification.NotificationWorker
import com.example.eduenglish.Retrofit.SearchViewModel
import com.example.eduenglish.Room.AppDataBase
import com.example.eduenglish.ViewModels.Other.SettingsViewModel
import com.example.eduenglish.Routes.Routes
import com.example.eduenglish.Routes.SelectorRoutes
import com.example.eduenglish.ui.theme.EduEnglishTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {

    val db by lazy { AppDataBase.getInstance(application) }
    val deckDao by lazy { db.deckDao() }
    val cardDao by lazy { db.cardDao() }
    val searchViewModel by lazy { SearchViewModel(deckDao, cardDao) }

    // Переменные для темы приложения
    private val settingsViewModel by lazy { SettingsViewModel(application) }

    //====================================================================================================

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //====================================================================================================

            // Notification запрос
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        101
                    )
                }
            }

            // Notification
            manageNotificationWork()

            //====================================================================================================

            // Navigation Controller
            val navController = rememberNavController()

            // Настройки для BottomBar
            val selectorRoutes = listOf(
                SelectorRoutes("Home", Routes.Home.route, Icons.Sharp.Home),
                SelectorRoutes("Search", Routes.Search.route, Icons.Sharp.Search),
                SelectorRoutes("Settings", Routes.Settings.route, Icons.Default.Settings)
            )

            // Переменные на навигацию
            val navBack by navController.currentBackStackEntryAsState()
            val currentDestination1 = navBack?.destination

            //====================================================================================================

            // Переменные для темы приложения
            val currentTheme by settingsViewModel.theme.collectAsState()

            //====================================================================================================

            EduEnglishTheme(appTheme = currentTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(
                            contentColor = MaterialTheme.colorScheme.surface,
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                        ) {
                            selectorRoutes.forEach { selector ->
                                BottomNavigationItem(
                                    selected = currentDestination1?.route == selector.route,
                                    onClick = {navController.navigate(selector.route)},
                                    icon = {
                                        androidx.compose.material.Icon(selector.icon,contentDescription = "иконка")
                                           },
                                    label = { Text(text = selector.name) },
                                )

                            }
                        }

                    }, // bottomBar
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home.route,
                    ) {
                        composable(Routes.Home.route) {
                            Home(navController)
                        }

                        composable(Routes.Settings.route) {
                            Settings(navController, settingsViewModel)
                        }

                        composable(Routes.Search.route) {
                            Search(viewModel = searchViewModel)
                        }

                        //====================================================================================================

                        composable(Routes.CreateDeck.route) {
                            CreateDeck(navController)
                        }

                        composable(
                            route = Routes.DeckVisual.route,
                            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
                            DeckVisual(navController, deckId)
                        }

                        composable("CreateCard/{deckId}") { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull()
                            deckId?.let {
                                CreateCard(deckId = it, navController = navController)
                            }
                        }

                        composable(
                            route = Routes.CardVisual.route,
                            arguments = listOf(navArgument("cardId") { type = NavType.IntType })
                        ) {
                            val cardId = it.arguments?.getInt("cardId") ?: 0
                            CardVisual(cardId = cardId, navController = navController)
                        }

                        composable(
                            route = Routes.StudyScreen.route,
                            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
                            StudyScreen(deckId = deckId, navController = navController)
                        }

                    }

                }// Scaffold

            }// EduEnglishTheme

        }// setContent

    }// fun onCreate

    //Notification
    private fun manageNotificationWork() {

        val enabled = runBlocking { settingsViewModel.notificationsEnabled.first() }
        val workManager = WorkManager.getInstance(applicationContext)

        if (enabled) {
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(36, java.util.concurrent.TimeUnit.HOURS)
                .setInitialDelay(36, java.util.concurrent.TimeUnit.HOURS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "review_reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        } else {
            workManager.cancelUniqueWork("review_reminder")
        }
    }

}// class MainActivity
