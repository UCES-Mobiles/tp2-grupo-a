package com.example.imdbcito.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdbcito.R
import com.example.imdbcito.data.models.entities.movie.MovieModel

class MovieAdapter(private val onItemClick: (MovieModel) -> Unit) :
    ListAdapter<MovieModel, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        private val ratingText: TextView = itemView.findViewById(R.id.movieRating)

        fun bind(movie: MovieModel) {
            titleText.text = movie.title
            ratingText.text = "⭐ ${String.format("%.1f", movie.voteAverage ?: 0.0)}"

            // Cargar imagen con Glide
            val posterUrl = if (!movie.posterPath.isNullOrEmpty()) {
                "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            } else {
                ""
            }

            if (posterUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_error_placeholder)
                    .into(posterImage)
            } else {
                posterImage.setImageResource(R.drawable.ic_movie_placeholder)
            }
        }
    }

    companion object MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }
}