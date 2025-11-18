package com.example.imdbcito.data.network.movies

import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.models.apirest.MoviesResponseDto
import com.example.imdbcito.data.models.apirest.PeopleResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): Response<MovieDto>

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/discover/movie")
    suspend fun discoverMoviesByYear(
        @Query("year") year: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<MoviesResponseDto>

    @GET("3/search/person")
    suspend fun searchPeople(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): Response<PeopleResponseDto>
}