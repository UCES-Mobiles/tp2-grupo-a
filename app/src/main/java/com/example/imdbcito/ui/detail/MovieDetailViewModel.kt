package com.example.imdbcito.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel
import com.example.imdbcito.data.repository.MovieRepository
import com.example.imdbcito.data.models.entities.movie.ReleaseDateModel
import com.example.imdbcito.data.repository.FavoriteMovieRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository()
    private val favoriteRepository = FavoriteMovieRepository(application)

    val movie: LiveData<MovieDto> = repository.movie
    val error: LiveData<String> = repository.error
    private val _movieName = MutableLiveData<String>()
    val movieName: LiveData<String> = _movieName

    private val _movieShortDescription = MutableLiveData<String>()
    val movieShortDescription: LiveData<String> = _movieShortDescription

    private val _releaseDate = MutableLiveData<ReleaseDateModel?>()
    val releaseDateMatch: LiveData<ReleaseDateModel> = _releaseDate as LiveData<ReleaseDateModel>

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    fun fetchMovie(movieId: Int) {
        repository.fetchMovie(movieId)

        repository.movie.observeForever { movie ->
            movie?.let {
                _movieName.value = it.originalTitle ?: "Nombre no disponible"
                _movieShortDescription.value = it.tagline ?: "Descripcion no disponible"
                val releaseDateString = it.releaseDate
                if (releaseDateString == null ) {
                    _releaseDate.value = null
                } else {
                    val formattedDate = formatMatchDate(releaseDateString)
                    _releaseDate.value = ReleaseDateModel(formattedDate)
                }
                // Cargar estado de favoritos
                _isFavorite.value = favoriteRepository.isFavorite(it.id)

            }
        }

        repository.movie.observeForever { movie ->
            movie?.let {
                _movieName.value = it.originalTitle ?: "Nombre no disponible"
                _movieShortDescription.value = it.tagline ?: "Descripción no disponible"
            }
        }
    }

    fun toggleFavorite(movie: MovieDto) {
        if (_isFavorite.value == true) {
            // Quitar de favoritos
            val fav = favoriteRepository.getFavorites().firstOrNull { it.movieId == movie.id }
            fav?.let { favoriteRepository.removeFavorite(it.id) }
            _isFavorite.value = false
        } else {
            // Agregar a favoritos
            favoriteRepository.addFavorite(
                FavoriteMovieModel(
                    movieId = movie.id,
                    title = movie.originalTitle ?: ""
                )
            )
            _isFavorite.value = true
        }
    }


    private fun formatMatchDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Fecha lanzamiento No disponible"

        return try {
            // Admite fechas con o sin ceros
            val possibleFormats = listOf(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
                SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            )

            var date: Date? = null
            for (format in possibleFormats) {
                try {
                    date = format.parse(dateString)
                    if (date != null) break
                } catch (_: Exception) { }
            }

            if (date == null) return "Formato fecha inválido"

            val outputFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
            outputFormat.format(date).replaceFirstChar { it.uppercase() }

        } catch (_: Exception) {
            "Formato fecha inválido"
        }
    }
}
