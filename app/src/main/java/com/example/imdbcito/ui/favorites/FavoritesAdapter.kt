package com.example.imdbcito.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdbcito.R
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel

class FavoritesAdapter(
    private val onItemClick: (FavoriteMovieModel) -> Unit,
    private val onRemoveClick: (FavoriteMovieModel) -> Unit,
    private val onToggleWatchedClick: (FavoriteMovieModel) -> Unit
) : ListAdapter<FavoriteMovieModel, FavoritesAdapter.FavoritesViewHolder>(FavoritesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.bind(favorite)
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        private val statusText: TextView = itemView.findViewById(R.id.movieStatus)
        private val removeButton: ImageButton = itemView.findViewById(R.id.btnRemoveFavorite)

        fun bind(favorite: FavoriteMovieModel) {
            titleText.text = favorite.title

            // Configurar estado "Vista/Pendiente"
            if (favorite.watched) {
                statusText.text = "✓ Vista"
                statusText.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                statusText.text = "Pendiente"
                statusText.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }

            // Cargar imagen de la película
            if (!favorite.posterPath.isNullOrEmpty()) {
                val posterUrl = "https://image.tmdb.org/t/p/w200${favorite.posterPath}"
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_error_placeholder)
                    .into(posterImage)
            } else {
                posterImage.setImageResource(R.drawable.ic_movie_placeholder)
            }

            // Click en el item completo → ir al detalle
            itemView.setOnClickListener {
                onItemClick(favorite)
            }

            // Click en el botón eliminar
            removeButton.setOnClickListener {
                onRemoveClick(favorite)
            }

            // Click en el estado "Vista/Pendiente"
            statusText.setOnClickListener {
                onToggleWatchedClick(favorite)
            }
        }
    }

    companion object FavoritesDiffCallback : DiffUtil.ItemCallback<FavoriteMovieModel>() {
        override fun areItemsTheSame(oldItem: FavoriteMovieModel, newItem: FavoriteMovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteMovieModel, newItem: FavoriteMovieModel): Boolean {
            return oldItem == newItem
        }
    }
}