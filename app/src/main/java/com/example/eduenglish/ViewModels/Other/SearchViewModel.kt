package com.example.eduenglish.ViewModels.Other

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.Retrofit.DTO.DeckDTO
import com.example.eduenglish.Retrofit.Server.RetrofitInstance

import com.example.eduenglish.Room.Dao.CardDao
import com.example.eduenglish.Room.Dao.DeckDao
import com.example.eduenglish.Room.Table.Card
import com.example.eduenglish.Room.Table.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SearchViewModel(
    private val deckDao: DeckDao,
    private val cardDao: CardDao
) : ViewModel() {
    private val _decks = MutableStateFlow<List<DeckDTO>>(emptyList())
    val decks: StateFlow<List<DeckDTO>> = _decks

    private val _loadingDeckId = MutableStateFlow<Int?>(null)
    val loadingDeckId: StateFlow<Int?> = _loadingDeckId

    init {
        fetchDecks()
    }

    private fun fetchDecks() {
        viewModelScope.launch {
            try {
                Log.d("SearchViewModel", "Запрос колод с сервера...")
                val response = RetrofitInstance.api.getDecks()
                Log.d("SearchViewModel", "Колод получено: ${response.size}")
                _decks.value = response
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Ошибка при получении колод: ${e.message}", e)
            }
        }
    }

    fun downloadDeck(deckDTO: DeckDTO) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "Начало скачивания колоды: ${deckDTO.name}")
            _loadingDeckId.value = deckDTO.id
            try {
                val localDeckId = deckDao.insert(
                    Deck(name = deckDTO.name)
                ).toInt()
                Log.d("SearchViewModel", "Колода сохранена в БД с id = $localDeckId")

                val cards = RetrofitInstance.api.getCardsForDeck(deckDTO.id)
                Log.d("SearchViewModel", "Карточек получено: ${cards.size}")

                cards.forEach { card ->
                    cardDao.insert(
                        Card(
                            deckId = localDeckId,
                            english = card.front,
                            russian = card.back
                        )
                    )
                }

                Log.d("SearchViewModel", "Карточки успешно сохранены в БД")

            } catch (e: Exception) {
                Log.e("SearchViewModel", "Ошибка при скачивании колоды: ${e.message}", e)
            } finally {
                _loadingDeckId.value = null
                Log.d("SearchViewModel", "Загрузка завершена для колоды: ${deckDTO.name}")
            }
        }
    }
}