package com.example.imdbcito.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbcito.R
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel

class FavoritesAdapter(
    private val onItemClick: (FavoriteMovieModel) -> Unit,
    private val onRemoveClick: (FavoriteMovieModel) -> Unit
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
        private val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        private val removeButton: ImageButton = itemView.findViewById(R.id.btnRemoveFavorite)

        fun bind(favorite: FavoriteMovieModel) {
            titleText.text = favorite.title

            itemView.setOnClickListener {
                onItemClick(favorite)
            }

            removeButton.setOnClickListener {
                onRemoveClick(favorite)
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