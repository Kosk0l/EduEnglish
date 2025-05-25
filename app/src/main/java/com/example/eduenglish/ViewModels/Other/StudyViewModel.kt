package com.example.eduenglish.ViewModels.Other

import androidx.lifecycle.ViewModel
import com.example.eduenglish.Room.Table.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.Room.AppDataBase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.app.Application
import kotlin.random.Random


class StudyViewModel(application: Application) : ViewModel() {

    private val cardDao = AppDataBase.getInstance(application).cardDao()

    private val _currentCard = MutableStateFlow<Card?>(null)
    val currentCard: StateFlow<Card?> = _currentCard

    private val studyCards = mutableListOf<Card>()


    //====================================================================================================

    fun loadCardsForDeck(deckId: Int) {
        viewModelScope.launch {
            cardDao.getCardsByDeck(deckId)
                .map { allCards ->
                    val now = System.currentTimeMillis()
                    allCards.filter { it.nextReviewTime <= now }
                }
                .collect { filteredCards ->
                    studyCards.clear()
                    filteredCards.forEach { resetSessionState(it) }
                    studyCards.addAll(filteredCards)
                    showRandomCard()
                }
        }
    }

    private fun showRandomCard() {
        if (studyCards.isEmpty()) {
            _currentCard.value = null
        } else {
            val randomIndex = Random.nextInt(studyCards.size)
            _currentCard.value = studyCards[randomIndex]
        }
    }

    private fun resetSessionState(card: Card) {
        card.badCount = 0
        card.normalCount = 0
        card.goodCount = 0
        card.consecutiveGood = 0
    }

    fun onAnswer(answerQuality: AnswerQuality) {
        val card = _currentCard.value ?: return


        val index = studyCards.indexOfFirst { it.id == card.id }
        if (index == -1) return


        when (answerQuality) {
            AnswerQuality.BAD -> {
                card.badCount++
                card.consecutiveGood = 0
            }
            AnswerQuality.NORMAL -> {
                card.normalCount++
                card.consecutiveGood = 0
            }
            AnswerQuality.GOOD -> {
                card.goodCount++
                card.consecutiveGood++
            }
        }

        if (card.consecutiveGood >= 2) {

            val nextInterval = calculateNextInterval(card)
            card.nextReviewTime = System.currentTimeMillis() + nextInterval

            viewModelScope.launch {
                cardDao.update(card)
            }

            studyCards.removeAt(index)
        } else {
            studyCards[index] = card
        }

        showRandomCard()
    }

    private fun calculateNextInterval(card: Card): Long {
        val score = 2 * card.goodCount - card.normalCount - 2 * card.badCount
        val hours = when {
            score >= 4 -> 36L
            score >= 2 -> 24L
            score >= 0 -> 12L
            else -> 6L
        }
        return TimeUnit.HOURS.toMillis(hours)
    }

    enum class AnswerQuality {
        BAD, NORMAL, GOOD
    }
}