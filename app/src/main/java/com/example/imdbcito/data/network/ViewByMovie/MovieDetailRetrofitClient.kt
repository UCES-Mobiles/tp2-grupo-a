package com.example.imdbcito.data.network.ViewByMovie

import android.util.Log
import com.example.imdbcito.data.network.common.RetrofitFactory.RetrofitFactory
import com.example.imdbcito.BuildConfig

object MovieDetailRetrofitClient {
    val movieDetailService: MovieDetailService by lazy {
        RetrofitFactory.createService(BuildConfig.BASE_DOMAIN, BuildConfig.API_TOKEN,
            MovieDetailService::class.java)
    }
}

