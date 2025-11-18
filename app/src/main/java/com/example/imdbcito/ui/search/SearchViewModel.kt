package com.example.imdbcito.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbcito.data.models.entities.movie.MovieModel
import com.example.imdbcito.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // MAPEO COMPLETO DE GÉNEROS TMDB CON IDs REALES
    private val genreMap = mapOf(
        "Acción" to 28,
        "Aventura" to 12,
        "Animación" to 16,
        "Comedia" to 35,
        "Crimen" to 80,
        "Documental" to 99,
        "Drama" to 18,
        "Familia" to 10751,
        "Fantasía" to 14,
        "Historia" to 36,
        "Terror" to 27,
        "Música" to 10402,
        "Misterio" to 9648,
        "Romance" to 10749,
        "Ciencia ficción" to 878,
        "Película de TV" to 10770,
        "Suspenso" to 53,
        "Bélica" to 10752,
        "Western" to 37
    )

    // Mapeo de décadas a años
    private val decadeMap = mapOf(
        "1950s" to 1950, "1960s" to 1960, "1970s" to 1970, "1980s" to 1980,
        "1990s" to 1990, "2000s" to 2000, "2010s" to 2010, "2020s" to 2020
    )

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.searchMovies(query)
            if (result.isSuccess) {
                val moviesResult = result.getOrNull() ?: emptyList()
                _movies.value = moviesResult
                if (moviesResult.isEmpty()) {
                    _error.value = "No se encontraron películas con: '$query'"
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error en la búsqueda"
                _movies.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun searchActors(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Usamos la función corregida del repository
            val result = repository.searchByActor(query)
            if (result.isSuccess) {
                val moviesResult = result.getOrNull() ?: emptyList()
                _movies.value = moviesResult
                if (moviesResult.isEmpty()) {
                    _error.value = "No se encontraron películas para el actor: '$query'"
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error buscando actor"
                _movies.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun searchByGenre(genreName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val genreId = genreMap[genreName]
            if (genreId != null) {
                val result = repository.searchByGenre(genreId)
                if (result.isSuccess) {
                    val moviesResult = result.getOrNull() ?: emptyList()
                    _movies.value = moviesResult
                    if (moviesResult.isEmpty()) {
                        _error.value = "No se encontraron películas del género: '$genreName'"
                    }
                } else {
                    _error.value = "Error buscando por género: ${result.exceptionOrNull()?.message}"
                    _movies.value = emptyList()
                }
            } else {
                _error.value = "Género no encontrado: '$genreName'. Usa la lista de sugerencias."
                _movies.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun searchByDecade(decade: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val startYear = decadeMap[decade]
            if (startYear != null) {
                val result = repository.searchByYear(startYear)
                if (result.isSuccess) {
                    val moviesResult = result.getOrNull() ?: emptyList()
                    _movies.value = moviesResult
                    if (moviesResult.isEmpty()) {
                        _error.value = "No se encontraron películas de la década: '$decade'"
                    }
                } else {
                    _error.value = "Error buscando por década: ${result.exceptionOrNull()?.message}"
                    _movies.value = emptyList()
                }
            } else {
                _error.value = "Década no válida: $decade"
                _movies.value = emptyList()
            }
            _isLoading.value = false
        }
    }
}