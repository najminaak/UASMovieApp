package com.example.uasmovieapp.adapter

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasmovieapp.activity.admin.EditActivity
import com.example.uasmovieapp.databinding.ItemMovieAdminBinding
import com.example.uasmovieapp.model.Movies
import com.example.uasmovieapp.network.ApiClient
import com.example.uasmovieapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesAdminAdapter(
    private val context: Context,
    private val movieList: MutableList<Movies>,
    private val onEditMovies: (Intent) -> Unit
) : RecyclerView.Adapter<MoviesAdminAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemMovieAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Movies) {
            with(binding) {
                tvTitle.text = data.title
                tvDirector.text = data.director
                Glide.with(root.context)
                    .load(data.image_url)
                    .into(ivMovie)

                btnEdit.setOnClickListener {
                    val intentToEdit = Intent(context, EditActivity::class.java)
                    intentToEdit.putExtra("id", data.id)
                    intentToEdit.putExtra("title", data.title)
                    intentToEdit.putExtra("director", data.director)
                    intentToEdit.putExtra("description", data.description)
                    intentToEdit.putExtra("release_date", data.release_date)
                    intentToEdit.putExtra("image_url", data.image_url)
                    context.startActivity(intentToEdit)
                }

                btnDelete.setOnClickListener {
                    deleteItem(data = data, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movieList.size

    fun deleteItem(data: Movies, adapterPosition: Int) {
        val client = ApiClient.getInstance()
        val response = data.id?.let { id -> client.deleteMovie(id) }
        response?.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Film ${data.title} berhasil dihapus", Toast.LENGTH_SHORT).show()
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        removeItem(adapterPosition)
                    }
                } else {
                    Log.e("API error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.e("API error", "Failed to delete movie: ${t.message}")
            }
        })
    }

    fun removeItem(position: Int) {
        movieList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateData(newList: List<Movies>) {
        movieList.clear()
        movieList.addAll(newList)
        notifyDataSetChanged()
    }
}

