package com.example.uasmovieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasmovieapp.R
import com.example.uasmovieapp.database.Bookmark
import com.example.uasmovieapp.databinding.ItemMovieBinding
import com.example.uasmovieapp.model.Movies

class BookmarkAdapter(private val bookmarks: List<Bookmark>,
                      private val onRemoveBookmarkClicked: (Bookmark) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: Bookmark, onRemoveBookmarkClicked: (Bookmark) -> Unit) {
            binding.tvTitle.text = bookmark.title
            binding.tvDirector.text = bookmark.director

//            binding.btnRemove.setOnClickListener {
//                onRemoveBookmarkClicked(bookmark)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarks[position], onRemoveBookmarkClicked)
    }

    override fun getItemCount(): Int = bookmarks.size
}

