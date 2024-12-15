package com.example.uasmovieapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmovieapp.adapter.MoviesAdapter
import com.example.uasmovieapp.databinding.FragmentHomeBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MoviesAdapter // adapter untuk menampilkan daftar film

    // membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // instance dari ApiClient untuk mendapatkan data film
        val client = ApiClient.getInstance()

        // ambil daftar film dari API
        val responseMovies = client.getAllMovies()

        // jalanin request API pake retrofit
        responseMovies.enqueue(object : Callback<List<Movies>> {
            override fun onResponse(call: Call<List<Movies>>, response: Response<List<Movies>>) {
                if (response.isSuccessful) {
                    val movieList = response.body()
                    if (movieList != null && movieList.isNotEmpty()) {
                        movieAdapter = MoviesAdapter(requireContext(), movieList)
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = movieAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Movies>>, t: Throwable) {
                Toast.makeText(requireContext(), "Koneksi Error", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
