package com.example.uasmovieapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasmovieapp.R
import com.example.uasmovieapp.activity.AboutActivity
import com.example.uasmovieapp.database.Bookmark
import com.example.uasmovieapp.database.MovieDao
import com.example.uasmovieapp.databinding.ItemMovieBinding
import com.example.uasmovieapp.model.Movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesAdapter(
    private val context: Context,
    private var movieList: List<Movies>,
    private val onAddBookmarkClicked: (Movies) -> Unit,
    private val mBookmarkDao: MovieDao
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movies, onAddBookmarkClicked: (Movies) -> Unit) {
            binding.tvTitle.text = movie.title
            binding.tvDirector.text = movie.director
            Glide.with(context).load(movie.image_url).into(binding.ivMovie)

            // Cek apakah film sudah di-bookmark
            val isBookmarked = checkIfBookmarked(movie)
            val bookmarkIcon = if (isBookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark_outline
            binding.ivBookmark.setImageResource(bookmarkIcon)

            // Set action untuk tombol bookmark
            binding.ivBookmark.setOnClickListener {
                if (isBookmarked) {
                    // Jika sudah di-bookmark, hapus dari bookmark
                    removeBookmark(movie)
                } else {
                    // Jika belum di-bookmark, tambahkan ke bookmark
                    addBookmark(movie)
                }
                notifyItemChanged(adapterPosition) // Perbarui item setelah diubah
            }
        }

        private fun checkIfBookmarked(movie: Movies): Boolean {
            return mBookmarkDao.getAllBookmark().value?.any { it.title == movie.title } == true
        }

        private fun addBookmark(movie: Movies) {
            val bookmark = Bookmark(
                title = movie.title,
                director = movie.director,
                image_url = movie.image_url
            )
            CoroutineScope(Dispatchers.IO).launch {
                mBookmarkDao.insert(bookmark)  // Menambahkan ke bookmark
            }
            Toast.makeText(context, "Bookmark berhasil disimpan", Toast.LENGTH_SHORT).show()
        }

        private fun removeBookmark(movie: Movies) {
            // Konversi dari Movies ke Bookmark
            val bookmark = Bookmark(
                title = movie.title,
                director = movie.director,
                image_url = movie.image_url
            )

            // Hapus dari database
            CoroutineScope(Dispatchers.IO).launch {
                mBookmarkDao.delete(bookmark)  // Menghapus bookmark berdasarkan objek Bookmark
            }
            Toast.makeText(context, "Bookmark berhasil dihapus", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position], onAddBookmarkClicked)
    }

    override fun getItemCount(): Int = movieList.size
}
