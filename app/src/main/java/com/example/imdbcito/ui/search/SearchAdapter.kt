package com.example.imdbcito.ui.search

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

class SearchAdapter(private val onItemClick: (MovieModel) -> Unit) :
    ListAdapter<MovieModel, SearchAdapter.SearchViewHolder>(SearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        private val overviewText: TextView = itemView.findViewById(R.id.movieOverview)
        private val ratingText: TextView = itemView.findViewById(R.id.movieRating)
        private val releaseDateText: TextView = itemView.findViewById(R.id.movieReleaseDate)

        fun bind(movie: MovieModel) {
            titleText.text = movie.title
            overviewText.text = movie.overview ?: "Descripción no disponible"
            ratingText.text = "⭐ ${String.format("%.1f", movie.voteAverage ?: 0.0)}"
            releaseDateText.text = movie.releaseDate ?: "Fecha no disponible"

            // Cargar imagen
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

    companion object SearchDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }
}