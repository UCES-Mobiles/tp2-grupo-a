package com.example.imdbcito.data.network.common.RetrofitFactory

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitFactory {

    fun <T> createService(baseUrl: String, serviceClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(serviceClass)
    }
}