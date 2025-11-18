package com.example.imdbcito.data.network.movies

import com.example.imdbcito.data.network.common.RetrofitFactory
import com.example.imdbcito.BuildConfig

object MovieRetrofitClient {
    val movieService: MovieService by lazy {
        RetrofitFactory.createService(
            BuildConfig.BASE_DOMAIN,
            BuildConfig.API_TOKEN,
            MovieService::class.java
        )
    }
}