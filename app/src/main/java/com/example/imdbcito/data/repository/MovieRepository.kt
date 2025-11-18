package com.example.imdbcito.data.repository

import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.models.apirest.MoviesResponseDto
import com.example.imdbcito.data.models.apirest.PeopleResponseDto
import com.example.imdbcito.data.models.entities.movie.MovieModel
import com.example.imdbcito.data.network.movies.MovieRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class MovieRepository {

    suspend fun getPopularMovies(page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getPopularMovies(page)
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error cargando populares: ${e.message}"))
        }
    }

    suspend fun getTopRatedMovies(page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getTopRatedMovies(page)
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error cargando mejor rankeadas: ${e.message}"))
        }
    }

    suspend fun getUpcomingMovies(page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<MoviesResponseDto> = MovieRetrofitClient.movieService.getUpcomingMovies(page)
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

    suspend fun searchMovies(query: String, page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response = MovieRetrofitClient.movieService.searchMovies(query, page)
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error buscando películas: ${e.message}"))
        }
    }

    suspend fun searchByGenre(genreId: Int, page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response = MovieRetrofitClient.movieService.discoverMoviesByGenre(genreId, page)
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error buscando por género: ${e.message}"))
        }
    }

    suspend fun searchByYear(year: Int, page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val response = MovieRetrofitClient.movieService.discoverMoviesByYear(year, page)
                handleMovieResponse(response)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error buscando por año: ${e.message}"))
        }
    }

    // BÚSQUEDA POR ACTOR CORREGIDA - USA LAS PELÍCULAS DEL CAMPO "known_for"
    suspend fun searchByActor(actorName: String, page: Int = 1): Result<List<MovieModel>> {
        return try {
            withContext(Dispatchers.IO) {
                // Buscar personas (actores)
                val peopleResponse = MovieRetrofitClient.movieService.searchPeople(actorName, page)

                if (peopleResponse.isSuccessful) {
                    val people = peopleResponse.body()
                    if (people != null && people.results.isNotEmpty()) {
                        // Tomar el primer actor encontrado
                        val actor = people.results.first()

                        // EXTRAER LAS PELÍCULAS DEL CAMPO "known_for"
                        val moviesFromKnownFor = actor.knownFor?.mapNotNull { movieDto ->
                            if (movieDto.mediaType == "movie") {
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
                            } else {
                                null // Ignorar series/tv
                            }
                        } ?: emptyList()

                        if (moviesFromKnownFor.isNotEmpty()) {
                            Result.success(moviesFromKnownFor)
                        } else {
                            // Si no hay películas en known_for, buscar por nombre del actor
                            val moviesResponse = MovieRetrofitClient.movieService.searchMovies(actorName, page)
                            handleMovieResponse(moviesResponse)
                        }
                    } else {
                        Result.failure(Exception("No se encontró el actor: $actorName"))
                    }
                } else {
                    Result.failure(Exception("Error buscando actor: ${peopleResponse.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error buscando por actor: ${e.message}"))
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