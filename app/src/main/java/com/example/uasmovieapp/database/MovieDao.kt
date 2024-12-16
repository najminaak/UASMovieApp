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
    fun insert(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("SELECT * from bookmark_table")
    fun getAllBookmark(): LiveData<List<Bookmark>>

    @Query("DELETE FROM bookmark_table WHERE id = :id")
    suspend fun deleteById(id: Int)
}