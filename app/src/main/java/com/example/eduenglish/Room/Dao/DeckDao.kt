package com.example.eduenglish.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eduenglish.Room.Table.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Insert
    suspend fun insert(deck: Deck)

    @Delete
    suspend fun delete(deck: Deck)

    @Update
    suspend fun update(deck: Deck)

    //====================================================================================================

    @Query("SELECT * FROM deck")
    fun getAllDecks(): Flow<List<Deck>>

    @Query("SELECT * FROM deck WHERE id = :deckId LIMIT 1")
    fun getDeckById(deckId: Int): Flow<Deck?>

    @Query("DELETE FROM deck WHERE id = :deckId")
    suspend fun deleteDeckById(deckId: Int)

}


