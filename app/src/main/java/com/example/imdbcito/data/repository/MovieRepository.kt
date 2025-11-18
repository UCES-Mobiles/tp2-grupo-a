package com.example.imdbcito.data.repository

import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.models.apirest.MoviesResponseDto
import com.example.imdbcito.data.models.entities.movie.MovieModel
import com.example.imdbcito.data.network.movies.MovieRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class MovieRepository {

    suspend fun getPopularMovies(): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getPopularMovies()
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error cargando populares: ${e.message}"))
        }
    }

    suspend fun getTopRatedMovies(): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getTopRatedMovies()
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error cargando mejor rankeadas: ${e.message}"))
        }
    }

    suspend fun getUpcomingMovies(): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getUpcomingMovies()
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error cargando próximos estrenos: ${e.message}"))
        }
    }

    suspend fun getMovieById(movieId: Int): Result<MovieDto> {
        return try {
            withContext(Dispatchers.IO) {
                val response = MovieRetrofitClient.movieService.getMovieById(movieId)
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        Result.success(movie)
                    } else {
                        Result.failure(Exception("Película no encontrada"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            }
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    private fun handleMovieResponse(response: Response<MoviesResponseDto>): Result<List<MovieModel>> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val movies = body.results.map { movieDto ->
                    MovieModel(
                        id = movieDto.id,
                        title = movieDto.title ?: "Sin título",
                        overview = movieDto.overview ?: "Descripción no disponible",
                        posterPath = movieDto.posterPath,
                        releaseDate = movieDto.releaseDate ?: "Fecha no disponible",
                        voteAverage = movieDto.voteAverage ?: 0.0,
                        popularity = movieDto.popularity ?: 0.0,
                        genreIds = movieDto.genreIds ?: emptyList()
                    )
                }
                Result.success(movies)
            } else {
                Result.failure(Exception("Respuesta vacía del servidor"))
            }
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    }
}