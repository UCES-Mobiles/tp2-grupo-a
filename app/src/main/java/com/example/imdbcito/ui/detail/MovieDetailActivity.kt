package com.example.imdbcito.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
        binding.textTeamName.text = movieName
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
                binding.textTeamName.text = movie.originalTitle ?: movieName
                }
        }

        viewModel.error.observe(this) { errorMsg ->
            //showErrorState(errorMsg)
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

}
