package com.example.uasmovieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_table")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val director: String,

    val image_url: String
)
