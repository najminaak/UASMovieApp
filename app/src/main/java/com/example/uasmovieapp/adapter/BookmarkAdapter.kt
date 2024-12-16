package com.example.uasmovieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasmovieapp.R
import com.example.uasmovieapp.database.Bookmark
import com.example.uasmovieapp.database.MovieDao
import com.example.uasmovieapp.databinding.ItemMovieBinding
import com.example.uasmovieapp.model.Movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkAdapter(
    private val bookmarks: MutableList<Bookmark>,  // Ubah menjadi MutableList agar bisa dimodifikasi
    private val onRemoveBookmarkClicked: (Bookmark) -> Unit,
    private val mBookmarkDao: MovieDao
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: Bookmark) {
            binding.tvTitle.text = bookmark.title
            binding.tvDirector.text = bookmark.director
            Glide.with(binding.root.context).load(bookmark.image_url).into(binding.ivMovie)

            // Set action untuk tombol remove bookmark
            binding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)

            binding.ivBookmark.setOnClickListener {
                // Hapus bookmark dari database
                removeBookmark(bookmark)
            }
        }

        private fun removeBookmark(bookmark: Bookmark) {
            CoroutineScope(Dispatchers.IO).launch {
                mBookmarkDao.delete(bookmark)  // Menghapus bookmark berdasarkan objek Bookmark

                // Menghapus bookmark dari daftar dan memperbarui UI di thread utama
                CoroutineScope(Dispatchers.Main).launch {
                    bookmarks.remove(bookmark)  // Hapus dari daftar
                    notifyItemRemoved(adapterPosition)  // Memberi tahu adapter untuk menghapus item
                    Toast.makeText(binding.root.context, "Bookmark berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarks[position])  // Berikan data yang sesuai untuk bind
    }

    override fun getItemCount(): Int = bookmarks.size
}
