package com.example.imdbcito.data.network.ViewByMovie

import com.example.imdbcito.data.network.common.RetrofitFactory.RetrofitFactory
import com.example.imdbcito.BuildConfig



object MovieDetailRetrofitClient {
    val MovieDetailService: MovieDetailService by lazy {
        RetrofitFactory.createService(BuildConfig.BASE_DOMAIN, MovieDetailService::class.java)
    }
}

