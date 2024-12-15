package com.example.uasmovieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("movies")
data class Movies(
    @PrimaryKey
    @SerializedName("_id")
    val id: String = "",

    @SerializedName("title")
    val title: String,

    @SerializedName("director")
    val director: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("release_date")
    val release_date: String,

    @SerializedName("image_url")
    val image_url: String,
)