package com.example.uasmovieapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.uasmovieapp.R
import com.example.uasmovieapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ambil data dari Intent
        val movieTitle = intent.getStringExtra("movieTitle") ?: "Title Movie"
        val movieDirector = intent.getStringExtra("movieDirector") ?: "Director Name"
        val releaseDate = intent.getStringExtra("releaseDate") ?: "Release Date"
        val movieImageUrl = intent.getStringExtra("movieImageUrl") ?: ""
        val movieSynopsis = intent.getStringExtra("movieSynopsis") ?: "Synopsis not available."

        // set data ke tampilan menggunakan binding
        binding.textTitleMovie.text = movieTitle
        binding.textDirector.text = movieDirector
        binding.textDate.text = releaseDate
        binding.textDesc.text = movieSynopsis

        // gambar menggunakan Glide
        if (movieImageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(movieImageUrl)
                .into(binding.imageMovie)
        } else {
            binding.imageMovie.setImageResource(R.drawable.placeholder_image)
        }

        // Handle tombol back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnWatch.setOnClickListener {
            val url = "https://www.netflix.com/signup"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse(url)
            }
            startActivity(intent)
        }
    }
}
