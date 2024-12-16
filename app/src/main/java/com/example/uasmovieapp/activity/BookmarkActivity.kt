package com.example.uasmovieapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmovieapp.database.MovieRoomDatabase
import com.example.uasmovieapp.databinding.ActivityBookmarkBinding
import androidx.lifecycle.Observer
import com.example.uasmovieapp.adapter.BookmarkAdapter
import com.example.uasmovieapp.database.Bookmark
import com.example.uasmovieapp.database.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var mMovieDao: MovieDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Inisialisasi database
        val db = MovieRoomDatabase.getDatabase(this)
        mMovieDao = db!!.bookmarkDao()!!
        executorService = Executors.newSingleThreadExecutor()

        // Panggil method untuk menampilkan data
        loadBookmarks()
    }

    private fun deleteBookmark(bookmark: Bookmark) {
        CoroutineScope(Dispatchers.IO).launch {
            mMovieDao.delete(bookmark)  // Hapus bookmark dari database
            withContext(Dispatchers.Main) {
                loadBookmarks()  // Refresh data setelah dihapus
            }
        }
        Toast.makeText(this, "Bookmark berhasil dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun loadBookmarks() {
        mMovieDao.getAllBookmark().observe(this) { bookmarks ->
            // Mengonversi daftar menjadi MutableList agar bisa dimodifikasi
            val bookmarkAdapter = BookmarkAdapter(bookmarks.toMutableList(), { bookmark ->
                deleteBookmark(bookmark)  // Menghapus bookmark ketika di klik
            }, mMovieDao)

            // Menetapkan layout dan adapter ke RecyclerView
            binding.rvBookmarks.layoutManager = LinearLayoutManager(this)
            binding.rvBookmarks.adapter = bookmarkAdapter
        }
    }
}
