package com.example.imdbcito.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbcito.data.models.entities.movie.MovieModel
import com.example.imdbcito.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String> = _currentCategory

    fun loadPopularMovies() {
        _currentCategory.value = "Populares"
        loadMovies { repository.getPopularMovies() }
    }

    fun loadTopRatedMovies() {
        _currentCategory.value = "Mejor Rankeadas"
        loadMovies { repository.getTopRatedMovies() }
    }

    fun loadUpcomingMovies() {
        _currentCategory.value = "Próximos Estrenos"
        loadMovies { repository.getUpcomingMovies() }
    }

    private fun loadMovies(apiCall: suspend () -> Result<List<MovieModel>>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = apiCall()
            if (result.isSuccess) {
                val movieList = result.getOrNull() ?: emptyList()
                _movies.value = movieList

                if (movieList.isEmpty()) {
                    _error.value = "No se encontraron películas"
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                // Mantener películas anteriores si hay error
                if (_movies.value.isNullOrEmpty()) {
                    _movies.value = emptyList()
                }
            }
            _isLoading.value = false
        }
    }
}