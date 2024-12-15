package com.example.uasmovieapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uasmovieapp.model.Movies

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: Movies)

    @Update
    fun update(movies: Movies)

    @Delete
    fun delete(movies: Movies)

    @get:Query("SELECT * from movies")
    val allMovies: LiveData<List<Movies>>
}