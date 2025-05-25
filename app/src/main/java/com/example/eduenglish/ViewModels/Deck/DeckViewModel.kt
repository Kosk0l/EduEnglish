package com.example.eduenglish.ViewModels.Deck

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.Room.AppDataBase
import com.example.eduenglish.Room.Table.Deck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private val deckDao = AppDataBase.getInstance(application).deckDao()
    private val cardDao = AppDataBase.getInstance(application).cardDao()

    // Показ колод
    val allDecks: Flow<List<Deck>> = deckDao.getAllDecks()

    // Показ Колоды (единично)
    fun getDeckById(deckId: Int): Flow<Deck?> = deckDao.getDeckById(deckId)

    // Удаление колоды с карточками
    fun deleteDeckWithCards(deckId: Int) {
        viewModelScope.launch {
            cardDao.deleteCardsByDeckId(deckId)
            deckDao.deleteDeckById(deckId)
        }
    }

    fun getLearnableCardCount(deckId: Int): Flow<Int> {
        return cardDao.getLearnableCardCount(deckId)
    }

    fun getDueCardCount(deckId: Int): Flow<Int> {
        val currentTime = System.currentTimeMillis()
        return cardDao.getDueCardCount(deckId, currentTime)
    }

}