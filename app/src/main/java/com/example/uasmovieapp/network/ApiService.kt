package com.example.uasmovieapp.network

import com.example.uasmovieapp.model.Movies
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("movies")
    fun getAllMovies() : Call<List<Movies>>

    // create
    @POST("movies")
    fun addMovie(@Body rawJson: RequestBody): Call<Movies>

    //update


    //delete
    @DELETE("movies/{id}")
    fun deleteMovie(@Path("id") movieId: String): Call<Movies>
}