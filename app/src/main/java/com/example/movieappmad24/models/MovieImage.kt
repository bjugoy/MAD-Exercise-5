package com.example.movieappmad24.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_images",
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["dbId"],
        childColumns = ["movieId"],
        onDelete = CASCADE
    )],
    indices = [Index("movieId"), Index("id")]
)
data class MovieImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieId: String,
    val url: String
)
