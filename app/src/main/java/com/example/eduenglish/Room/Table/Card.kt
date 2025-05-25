package com.example.eduenglish.Room.Table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "card",
    foreignKeys = [ForeignKey(
        entity = Deck::class,
        parentColumns = ["id"],
        childColumns = ["deckId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Card(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deckId: Int,
    val english: String,
    val russian: String,

    var badCount: Int = 0,
    var normalCount: Int = 0,
    var goodCount: Int = 0,

    var consecutiveGood: Int = 0,

    var nextReviewTime: Long = 0L
)