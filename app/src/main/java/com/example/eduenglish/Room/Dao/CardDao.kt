package com.example.eduenglish.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eduenglish.Room.Table.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)

    //====================================================================================================

    @Query("SELECT * FROM card WHERE deckId = :deckId")
    fun getCardsByDeck(deckId: Int): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id = :cardId LIMIT 1")
    fun getCardById(cardId: Int): Flow<Card?>

    @Query("SELECT * FROM card WHERE id = :cardId LIMIT 1")
    suspend fun getCardByIdOnce(cardId: Int): Card?

    @Query("DELETE FROM card WHERE deckId = :deckId")
    suspend fun deleteCardsByDeckId(deckId: Int)

    //====================================================================================================

    @Query("SELECT COUNT(*) FROM card WHERE deckId = :deckId")
    fun getLearnableCardCount(deckId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM card WHERE deckId = :deckId AND nextReviewTime <= :currentTime")
    fun getDueCardCount(deckId: Int, currentTime: Long): Flow<Int>

}