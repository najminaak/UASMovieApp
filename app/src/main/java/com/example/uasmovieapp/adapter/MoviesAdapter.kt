package com.example.uasmovieapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasmovieapp.activity.AboutActivity
import com.example.uasmovieapp.databinding.ItemMovieBinding
import com.example.uasmovieapp.model.Movies

class MoviesAdapter(private val context: Context, private val movieList: List<Movies>,
                    private val onAddBookmarkClicked: (Movies) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie, onAddBookmarkClicked)
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Open AboutActivity when the movie item is clicked
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movieList[position]
                    val intent = Intent(it.context, AboutActivity::class.java).apply {
                        putExtra("movieId", selectedMovie.id)
                        putExtra("movieTitle", selectedMovie.title)
                        putExtra("movieDirector", selectedMovie.director)
                        putExtra("releaseDate", selectedMovie.release_date)
                        putExtra("movieImageUrl", selectedMovie.image_url)
                        putExtra("movieSynopsis", selectedMovie.description)
                    }
                    it.context.startActivity(intent)
                }
            }
        }

        fun bind(movie: Movies, onAddBookmarkClicked: (Movies) -> Unit) {
            // Set movie title, director and image
            binding.tvTitle.text = movie.title
            binding.tvDirector.text = movie.director
            Glide.with(binding.root.context)
                .load(movie.image_url)
                .into(binding.ivMovie)

            binding.ivBookmark.setOnClickListener {
                onAddBookmarkClicked(movie)
            }
        }
    }
}
