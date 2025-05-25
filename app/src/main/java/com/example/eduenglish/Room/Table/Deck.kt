package com.example.eduenglish.Room.Table

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "deck")
data class Deck (

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)
