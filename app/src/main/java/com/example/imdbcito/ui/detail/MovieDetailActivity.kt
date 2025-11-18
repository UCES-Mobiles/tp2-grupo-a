package com.example.imdbcito.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.imdbcito.R
import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        setupUI()
        observeViewModel()
        viewModel.fetchMovie(movieId)
    }

    private fun getIntentData() {
        movieId = intent.getIntExtra("MOVIE_ID", 0)
        // Mostrar "Cargando..." temporalmente
        binding.textMovieName.text = "Cargando..."
        binding.textReleaseDate.text = "Cargando..."
        binding.textShortDescription.text = "Cargando..."
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        viewModel.movie.observe(this) { movie ->
            movie?.let {
                // MÓDULO 1: Información Básica
                // Nombre de película
                binding.textMovieName.text = movie.originalTitle ?: movie.title ?: "Nombre no disponible"

                // Descripción corta
                binding.textShortDescription.text = movie.tagline ?: "Descripción no disponible"

                // Cargar imagen
                loadMoviePic(it)

                // Actualizar action bar
                supportActionBar?.title = movie.originalTitle ?: movie.title ?: "Detalle"

                // Configurar botón favoritos
                binding.btnFavorites.setOnClickListener {
                    viewModel.toggleFavorite(movie)
                }
            }
        }

        // Observar fecha de lanzamiento (puede ser null)
        viewModel.releaseDateMatch.observe(this) { match ->
            binding.textReleaseDate.text = match?.formattedDate ?: "Fecha no disponible"
        }

        // Observar estado de favoritos
        viewModel.isFavorite.observe(this) { isFav ->
            val drawable = if (isFav) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            binding.btnFavorites.setImageResource(drawable)
        }

        // Observar errores
        viewModel.error.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        // Observar loading
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.textMovieName.text = "Cargando..."
                binding.textReleaseDate.text = "Cargando..."
                binding.textShortDescription.text = "Cargando..."
            }
        }
    }

    private fun loadMoviePic(movie: MovieDto) {
        val posterUrl = movie.posterPath
        if (!posterUrl.isNullOrEmpty()) {
            // Construir URL completa para TMDB
            val fullPosterUrl = "https://image.tmdb.org/t/p/w500$posterUrl"
            Glide.with(this)
                .load(fullPosterUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .centerInside()
                .into(binding.moviePic)
        } else {
            binding.moviePic.setImageResource(R.drawable.ic_movie_placeholder)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}