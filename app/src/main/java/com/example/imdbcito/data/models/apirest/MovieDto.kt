package com.example.imdbcito.data.models.apirest

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("media_type") val mediaType: String? // NUEVO: para known_for
)

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class MoviesResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

// Modelo para búsqueda de personas (actores)
data class PersonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for") val knownFor: List<MovieDto>? // NUEVO: películas conocidas
)

data class PeopleResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<PersonDto>, // CORREGIDO: faltaba "results"
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)