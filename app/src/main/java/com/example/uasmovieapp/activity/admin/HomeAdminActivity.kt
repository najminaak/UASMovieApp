package com.example.uasmovieapp.activity.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmovieapp.adapter.MoviesAdminAdapter
import com.example.uasmovieapp.databinding.ActivityHomeAdminBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import com.example.uasmovieapp.utils.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var adapter: MoviesAdminAdapter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val movieList = ArrayList<Movies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMovies()

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = MoviesAdminAdapter(this, movieList)
        binding.recyclerViewAdmin.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdmin.adapter = adapter
    }

    private fun fetchMovies() {
        val client = ApiClient.getInstance()
        val responseMovies = client.getAllMovies()

        responseMovies.enqueue(object : Callback<List<Movies>> {
            override fun onResponse(call: Call<List<Movies>>, response: Response<List<Movies>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        movieList.clear()
                        movieList.addAll(movies)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Movies>>, t: Throwable) {
                showToast("Koneksi Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}