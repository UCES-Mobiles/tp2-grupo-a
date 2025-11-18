package com.example.imdbcito.data.network.common.RetrofitFactory

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitFactory {

    fun <T> createService(baseUrl: String, token: String? = null,serviceClass: Class<T>): T {

        val httpClientBuilder = OkHttpClient.Builder()

        // Si hay token, agregar interceptor para el header
        token?.let {
            httpClientBuilder.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(serviceClass)
    }
}

