package com.example.uasmovieapp.activity.admin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.bumptech.glide.Glide
import com.example.uasmovieapp.R
import com.example.uasmovieapp.databinding.ActivityAddBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            editMovieImage.addTextChangedListener { editable ->
                val imageUrl = editable.toString()
                if (imageUrl.isNotEmpty()) {
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(binding.imageMovie)
                    binding.imageMovie.visibility = View.VISIBLE
                } else {
                    binding.imageMovie.visibility = View.GONE
                }
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnAdd.setOnClickListener{
                val titleInput = editMovieTitle.text.toString()
                val directorInput = editMovieDirector.text.toString()
                val descriptionInput = editMovieDesc.text.toString()
                val release_dateInput = editMovieDate.text.toString()
                val image_urlInput = editMovieImage.text.toString()

                addDataMovies(titleInput, directorInput, descriptionInput, release_dateInput, image_urlInput)
            }
        }
    }
    private fun addDataMovies(
        title: String,
        director: String,
        description: String,
        release_date: String,
        image_url:String
    ) {
        val jsonData = Gson().toJson(
            mapOf(
                "title" to title,
                "director" to director,
                "description" to description,
                "release_date" to release_date,
                "image_url" to image_url
            )
        )
        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        val client = ApiClient.getInstance()
        val response = client.addMovie(requestBody)
        response.enqueue(object : Callback<Movies> {
            override fun onResponse(p0: Call<Movies>, p1: Response<Movies>) {
                if (p1.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@AddActivity, "Berhasil tambah film", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API error", "Response not successful or body is null")
                    Toast.makeText(this@AddActivity, "Gagal menambah film", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(p0: Call<Movies>, p1: Throwable) {
                Toast.makeText(this@AddActivity, "Koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}