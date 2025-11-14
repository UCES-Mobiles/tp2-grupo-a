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
    private var movieName: String = ""

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
        binding.textMovieName.text = movieName
        binding.textReleaseDate.text = movieName
    }

    private fun setupUI() {
        // Setup action bar with team name
        supportActionBar?.title = movieName
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        viewModel.movie.observe(this) { movie ->
            movie?.let {
                // MÓDULO 1: Información Básica
                //Nombre de pelicula
                binding.textMovieName.text = movie.originalTitle ?: movieName
                //Fecha de lanzamiento
                viewModel.releaseDateMatch.observe(this) { match ->
                    binding.textReleaseDate.text = match.formattedDate
                    }
                binding.textShortDescription.text = movie.tagline ?: "Descripcion no disponible"
                loadMoviePic(it)
                }
        }

        viewModel.movieName.observe(this) { name ->
            binding.textMovieName.text = name
            supportActionBar?.title = name
        }

        viewModel.movieShortDescription.observe(this) { description ->
            binding.textShortDescription.text = description
        }

        viewModel.error.observe(this) { errorMsg ->
            //showErrorState(errorMsg)
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadMoviePic(movie: MovieDto) {
        val logoUrl = movie.posterPath
        if (!logoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(logoUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .centerInside()
                .into(binding.moviePic)
        } else {
            binding.moviePic.setImageResource(R.drawable.ic_movie_placeholder)
        }
    }



}
