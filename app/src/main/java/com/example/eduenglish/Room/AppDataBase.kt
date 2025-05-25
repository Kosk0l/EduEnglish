package com.example.eduenglish.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eduenglish.Room.Dao.CardDao
import com.example.eduenglish.Room.Dao.DeckDao
import com.example.eduenglish.Room.Table.Card
import com.example.eduenglish.Room.Table.Deck
import android.content.Context
import androidx.room.Room

@Database(entities = [Deck::class, Card::class], version = 3)
abstract class AppDataBase : RoomDatabase() {

    abstract fun deckDao(): DeckDao

    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "edu_english_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

