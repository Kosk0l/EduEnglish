package com.example.eduenglish.Retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.Room.Dao.CardDao
import com.example.eduenglish.Room.Dao.DeckDao
import com.example.eduenglish.Room.Table.Card
import com.example.eduenglish.Room.Table.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class DeckDTO(
    val id: Int,
    val name: String,
    val description: String
)

data class CardDTO(
    val id: Int,
    val deckId: Int,
    val front: String,
    val back: String
)

interface ApiService {
    @GET("decks")
    suspend fun getDecks(): List<DeckDTO>

    @GET("decks/{id}/cards")
    suspend fun getCardsForDeck(@Path("id") deckId: Int): List<CardDTO>
}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}

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