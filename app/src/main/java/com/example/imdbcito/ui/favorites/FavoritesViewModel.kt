package com.example.imdbcito.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel
import com.example.imdbcito.data.repository.FavoriteMovieRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FavoriteMovieRepository(application)

    private val _favorites = MutableLiveData<List<FavoriteMovieModel>>()
    val favorites: LiveData<List<FavoriteMovieModel>> = _favorites

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val favoriteMovies = repository.getFavorites()
                _favorites.value = favoriteMovies
            } catch (e: Exception) {
                _error.value = "Error cargando favoritos: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    fun removeFavorite(movie: FavoriteMovieModel) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(movie.id)
                loadFavorites()
            } catch (e: Exception) {
                _error.value = "Error eliminando favorito: ${e.message}"
            }
        }
    }

    fun toggleWatchedStatus(movie: FavoriteMovieModel) {
        viewModelScope.launch {
            try {
                repository.updateWatchedStatus(movie.id, !movie.watched)
                loadFavorites()
            } catch (e: Exception) {
                _error.value = "Error actualizando estado: ${e.message}"
            }
        }
    }
}