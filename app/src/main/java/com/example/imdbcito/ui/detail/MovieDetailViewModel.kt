package com.example.imdbcito.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel
import com.example.imdbcito.data.models.entities.movie.ReleaseDateModel
import com.example.imdbcito.data.repository.MovieRepository
import com.example.imdbcito.data.repository.FavoriteMovieRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository()
    private val favoriteRepository = FavoriteMovieRepository(application)

    private val _movie = MutableLiveData<MovieDto?>()
    val movie: LiveData<MovieDto?> = _movie

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _releaseDate = MutableLiveData<ReleaseDateModel?>()
    val releaseDateMatch: LiveData<ReleaseDateModel?> = _releaseDate

    fun fetchMovie(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getMovieById(movieId)
            if (result.isSuccess) {
                val movieData = result.getOrNull()
                _movie.value = movieData

                // Procesar fecha de lanzamiento
                movieData?.releaseDate?.let { dateString ->
                    val formattedDate = formatMatchDate(dateString)
                    _releaseDate.value = ReleaseDateModel(formattedDate)
                } ?: run {
                    _releaseDate.value = null
                }

                // Cargar estado de favoritos
                _isFavorite.value = favoriteRepository.isFavorite(movieId)
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error al cargar la película"
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorite(movie: MovieDto) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                // Quitar de favoritos
                val fav = favoriteRepository.getFavorites().firstOrNull { it.movieId == movie.id }
                fav?.let { favoriteRepository.removeFavorite(it.id) }
                _isFavorite.value = false
            } else {
                // Agregar a favoritos CON POSTER PATH
                favoriteRepository.addFavorite(
                    FavoriteMovieModel(
                        movieId = movie.id,
                        title = movie.originalTitle ?: movie.title ?: "Sin título",
                        posterPath = movie.posterPath // IMPORTANTE: Guardar el poster
                    )
                )
                _isFavorite.value = true
            }
        }
    }

    private fun formatMatchDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
            outputFormat.format(date!!).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            "Fecha no disponible"
        }
    }
}