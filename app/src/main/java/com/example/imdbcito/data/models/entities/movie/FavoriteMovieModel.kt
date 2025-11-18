package com.example.imdbcito.data.models.entities.movie

data class FavoriteMovieModel(
    val id: Long = 0,
    val movieId: Int,
    val title: String,
)