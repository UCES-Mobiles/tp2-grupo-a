package com.example.imdbcito.data.models.entities.movie

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val popularity: Double?,
    val genreIds: List<Int>?
)