package com.example.eduenglish.Routes

sealed class Routes(val route: String) {

    // BottomBar
    object Home : Routes("Home")
    object Stats : Routes("Stats")
    object Settings : Routes("Settings")

    // Ответления от Home
    object CreateDeck : Routes("CreateDeck")

    object DeckVisual : Routes("DeckVisual/{deckId}") {
        fun createRoute(deckId: Int) = "DeckVisual/$deckId"
    }

    object CreateCard : Routes("CreateCard/{deckId}") {
        fun createRoute(deckId: Int) = "CreateCard/$deckId"
    }

    object CardVisual : Routes("CardVisual/{cardId}") {
        fun createRoute(cardId: Int) = "CardVisual/$cardId"
    }

    object StudyScreen : Routes("learn/{deckId}") {
        fun createRoute(deckId: Int) = "learn/$deckId"
    }

}