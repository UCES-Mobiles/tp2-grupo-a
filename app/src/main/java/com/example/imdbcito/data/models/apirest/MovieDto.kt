package com.example.imdbcito.data.models.apirest

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("tagline") val tagline: String?
)
