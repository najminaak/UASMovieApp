package com.example.uasmovieapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmovieapp.activity.BookmarkActivity
import com.example.uasmovieapp.adapter.MoviesAdapter
import com.example.uasmovieapp.database.Bookmark
import com.example.uasmovieapp.database.MovieDao
import com.example.uasmovieapp.database.MovieRoomDatabase
import com.example.uasmovieapp.databinding.FragmentHomeBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MoviesAdapter
    private lateinit var mBookmarkDao: MovieDao // Assuming BookmarkDao is the same as MovieDao

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
                        movieAdapter = MoviesAdapter(requireContext(), movieList, onAddBookmarkClicked = { movie ->
                            saveBookmark(movie)
                        })
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = movieAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Movies>>, t: Throwable) {
                Toast.makeText(requireContext(), "Koneksi Error", Toast.LENGTH_SHORT).show()
            }
        })

        val db = MovieRoomDatabase.getDatabase(requireContext())
        mBookmarkDao = db!!.bookmarkDao()!!

        binding.buttonToBookmark.setOnClickListener {
            startActivity(Intent(requireActivity(), BookmarkActivity::class.java))
        }

        return binding.root
    }

    // Save bookmark to the database
    private fun saveBookmark(movie: Movies) {
        val bookmark = Bookmark(
            title = movie.title,
            director = movie.director,
            image_url = movie.image_url
        )
        CoroutineScope(Dispatchers.IO).launch {
            mBookmarkDao.insert(bookmark)
        }
        Toast.makeText(requireContext(), "Bookmark berhasil disimpan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
