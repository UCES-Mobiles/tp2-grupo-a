package com.example.imdbcito.data.repository

import android.content.Context
import com.example.imdbcito.data.database.FavoriteMovieDbHelper
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel

class FavoriteMovieRepository(context: Context) {

    private val dbHelper = FavoriteMovieDbHelper(context)

    fun addFavorite(movie: FavoriteMovieModel): Long {
        return dbHelper.insertMovie(movie)
    }

    fun removeFavorite(id: Long): Int {
        return dbHelper.deleteMovie(id)
    }

    fun updateWatchedStatus(id: Long, watched: Boolean): Int {
        return dbHelper.updateWatchedStatus(id, watched)
    }

    fun getFavorites(): List<FavoriteMovieModel> {
        return dbHelper.getAllMovies()
    }

    fun isFavorite(movieId: Int): Boolean {
        return dbHelper.isFavorite(movieId)
    }
}