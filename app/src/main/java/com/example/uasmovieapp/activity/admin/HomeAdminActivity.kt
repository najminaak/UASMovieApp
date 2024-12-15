package com.example.uasmovieapp.activity.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmovieapp.activity.LoginRegisterActivity
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
    private lateinit var adapterMovies: MoviesAdminAdapter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val movieList = ArrayList<Movies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = PrefManager.getInstance(this)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapterMovies = MoviesAdminAdapter(
            context = this,
            movieList = movieList,
            onEditMovies = { intent ->
                activityResultLauncher.launch(intent)
            }
        )

        with(binding) {
            btnAdd.setOnClickListener {
                val intentToAdd = Intent(this@HomeAdminActivity, AddActivity::class.java)
                activityResultLauncher.launch(intentToAdd)
            }

            recyclerViewAdmin.apply {
                adapter = adapterMovies
                layoutManager = LinearLayoutManager(this@HomeAdminActivity)
            }

            buttonLogout.setOnClickListener {
                prefManager.clear()
                val intent = Intent(this@HomeAdminActivity, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish() // Menutup Activity saat ini
            }
        }

        fetchMovies()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fetchMovies() // Menarik data terbaru setelah mengedit film
            }
        }
    }

    private fun fetchMovies() {
        val client = ApiClient.getInstance()
        val responseMovies = client.getAllMovies()

        responseMovies.enqueue(object : Callback<List<Movies>> {
            override fun onResponse(call: Call<List<Movies>>, response: Response<List<Movies>>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateMovieList = response.body() ?: emptyList()
                    adapterMovies.updateData((updateMovieList))
                } else {
                    Log.e("API error", "Response not successful or body is null")
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
