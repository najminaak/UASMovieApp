package com.example.uasmovieapp.activity.admin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.uasmovieapp.databinding.ActivityEditBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Ambil data intent
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val director = intent.getStringExtra("director")
        val description = intent.getStringExtra("description")
        val release_date = intent.getStringExtra("release_date")
        val image_url = intent.getStringExtra("image_url")

        with(binding) {
            // Isi kolom dengan data awal
            editMovieTitle.setText(title.toString())
            editMovieDirector.setText(director.toString())
            editMovieDesc.setText(description.toString())
            editMovieDate.setText(release_date.toString())
            editMovieImage.setText(image_url.toString())

            // Muat gambar langsung jika URL ada
            if (image_url != null && image_url.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(image_url)
                    .into(binding.imageMovie)
                binding.imageMovie.visibility = View.VISIBLE
            } else {
                binding.imageMovie.visibility = View.GONE
            }

            // Load image preview saat URL dimasukkan
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

            btnSave.setOnClickListener {
                val updatedTitle = editMovieTitle.text.toString()
                val updatedDirector = editMovieDirector.text.toString()
                val updatedDescription = editMovieDesc.text.toString()
                val updatedReleaseDate = editMovieDate.text.toString()
                val updatedImageUrl = editMovieImage.text.toString()

                if (id != null) {
                    editDataMovies(id, updatedTitle, updatedDirector, updatedDescription, updatedReleaseDate, updatedImageUrl)
                } else {
                    Toast.makeText(this@EditActivity, "ID Film tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editDataMovies(
        id: String,
        title: String,
        director: String,
        description: String,
        release_date: String,
        image_url: String
    ) {
        // membuat data JSON untuk permintaan
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
        val response = client.updateMovies(id, requestBody)

        response.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@EditActivity, "Berhasil mengedit film", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API error", "Response not successful or body is null")
                    Toast.makeText(this@EditActivity, "Gagal mengedit film", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Toast.makeText(this@EditActivity, "Koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

