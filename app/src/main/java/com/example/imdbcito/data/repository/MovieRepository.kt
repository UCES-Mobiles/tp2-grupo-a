package com.example.imdbcito.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imdbcito.data.models.apirest.MovieDto
import com.example.imdbcito.data.network.ViewByMovie.MovieDetailRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {

    private val _movie = MutableLiveData<MovieDto>()
    val movie: LiveData<MovieDto> get() = _movie

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchMovie(movieId: Int) {
            val call = MovieDetailRetrofitClient.movieDetailService.getMovieById(movieId)
            call.enqueue(object : Callback<MovieDto> {
                override fun onResponse(
                    call: Call<MovieDto>,
                    response: Response<MovieDto>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _movie.postValue(body)
                        } else {
                            _error.postValue("Respuesta vacía")
                        }
                    } else {
                        _error.postValue("Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MovieDto>, t: Throwable) {
                    _error.postValue("Fallo de red: ${t.message}")
                }
            })
    }
}