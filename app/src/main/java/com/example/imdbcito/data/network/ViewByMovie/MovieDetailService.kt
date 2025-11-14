package com.example.imdbcito.data.network.ViewByMovie

import com.example.imdbcito.data.models.apirest.MovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailService {
    @GET("3/movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") movieId: Int
    ): Call<MovieDto>
}
