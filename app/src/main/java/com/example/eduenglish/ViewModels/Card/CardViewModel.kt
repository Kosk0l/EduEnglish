package com.example.eduenglish.ViewModels.Card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.Room.AppDataBase
import com.example.eduenglish.Room.Table.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDataBase.getInstance(application).cardDao()

    // Показ Карточек
    fun getCardsForDeck(deckId: Int): Flow<List<Card>> {
        return dao.getCardsByDeck(deckId)
    }

    // Показ Карточки
    fun getCardById(cardId: Int): Flow<Card?> {
        return dao.getCardById(cardId)
    }

    // Удалить карточку
    fun deleteCard(card: Card) {
        viewModelScope.launch {
            dao.delete(card)
        }
    }

    // Изменить карточку
    fun updateCard(card: Card) {
        viewModelScope.launch {
            dao.update(card)
        }
    }

}