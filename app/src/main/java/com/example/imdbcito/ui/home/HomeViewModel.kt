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
    private var currentPage = 1
    private var isLoadingPage = false

    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String> = _currentCategory

    private var currentApiCall: (suspend (Int) -> Result<List<MovieModel>>)? = null

    fun loadPopularMovies() {
        currentPage = 1
        _currentCategory.value = "Populares"
        currentApiCall = { page -> repository.getPopularMovies(page) }
        loadMovies()
    }

    fun loadTopRatedMovies() {
        currentPage = 1
        _currentCategory.value = "Mejor Rankeadas"
        currentApiCall = { page -> repository.getTopRatedMovies(page) }
        loadMovies()
    }

    fun loadUpcomingMovies() {
        currentPage = 1
        _currentCategory.value = "Próximos Estrenos"
        currentApiCall = { page -> repository.getUpcomingMovies(page) }
        loadMovies()
    }

    fun loadNextPage() {
        if (!isLoadingPage && currentApiCall != null) {
            currentPage++
            loadMovies(true)
        }
    }

    private fun loadMovies(isLoadingMore: Boolean = false) {
        if (isLoadingPage) return

        viewModelScope.launch {
            isLoadingPage = true
            if (!isLoadingMore) {
                _isLoading.value = true
            }
            _error.value = null

            val apiCall = currentApiCall ?: return@launch

            val result = apiCall(currentPage)
            if (result.isSuccess) {
                val newMovies = result.getOrNull() ?: emptyList()
                if (isLoadingMore) {
                    // Agregar a la lista existente
                    val currentMovies = _movies.value ?: emptyList()
                    _movies.value = currentMovies + newMovies
                } else {
                    // Reemplazar la lista
                    _movies.value = newMovies
                }

                if (newMovies.isEmpty() && currentPage > 1) {
                    _error.value = "No hay más películas para cargar"
                    currentPage-- // Revertir el incremento
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                if (isLoadingMore) {
                    currentPage-- // Revertir el incremento en caso de error
                }
            }

            _isLoading.value = false
            isLoadingPage = false
        }
    }

    fun canLoadMore(): Boolean {
        return !isLoadingPage && currentApiCall != null
    }
}