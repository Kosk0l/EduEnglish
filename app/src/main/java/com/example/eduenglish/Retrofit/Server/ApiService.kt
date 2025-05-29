package com.example.eduenglish.Retrofit.Server

import com.example.eduenglish.Retrofit.DTO.CardDTO
import com.example.eduenglish.Retrofit.DTO.DeckDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("decks")
    suspend fun getDecks(): List<DeckDTO>

    @GET("decks/{id}/cards")
    suspend fun getCardsForDeck(@Path("id") deckId: Int): List<CardDTO>
}